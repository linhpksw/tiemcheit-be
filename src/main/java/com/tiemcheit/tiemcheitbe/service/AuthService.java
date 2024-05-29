package com.tiemcheit.tiemcheitbe.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.tiemcheit.tiemcheitbe.dto.request.AuthRequest;
import com.tiemcheit.tiemcheitbe.dto.request.IntrospectRequest;
import com.tiemcheit.tiemcheitbe.dto.request.LogoutRequest;
import com.tiemcheit.tiemcheitbe.dto.request.RefreshRequest;
import com.tiemcheit.tiemcheitbe.dto.response.AuthResponse;
import com.tiemcheit.tiemcheitbe.dto.response.IntrospectResponse;
import com.tiemcheit.tiemcheitbe.exception.AppException;
import com.tiemcheit.tiemcheitbe.model.InvalidatedToken;
import com.tiemcheit.tiemcheitbe.model.User;
import com.tiemcheit.tiemcheitbe.repository.InvalidatedTokenRepo;
import com.tiemcheit.tiemcheitbe.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final UserRepo userRepo;
    private final InvalidatedTokenRepo invalidatedTokenRepo;

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long accessTokenExpiration;

    @Value("${security.jwt.refresh-token-expiration-time}")
    private long refreshTokenExpiration;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(secretKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
        Date issueTime = claims.getIssueTime();
        String tokenType = (String) claims.getClaim("type");
        Date expiryTime;
        String jti = claims.getJWTID();

        if ("refresh".equals(tokenType) && !isRefresh) {
            throw new AppException("Refresh token can not use for access", HttpStatus.FORBIDDEN);
        } else if ("access".equals(tokenType) && isRefresh) {
            throw new AppException("Access token can not use for refresh", HttpStatus.FORBIDDEN);
        }

        expiryTime = claims.getExpirationTime();

        if (isRefresh) {
            expiryTime = new Date(issueTime.toInstant().plus(refreshTokenExpiration, ChronoUnit.SECONDS).toEpochMilli());
        }

        boolean verified = signedJWT.verify(verifier);

        if (!verified || !expiryTime.after(new Date())) {
            throw new AppException("Token is invalid", HttpStatus.UNAUTHORIZED);
        }

        if (invalidatedTokenRepo.existsById(jti)) {
            throw new AppException("Token is invalid", HttpStatus.UNAUTHORIZED);
        }

        return signedJWT;
    }

    public AuthResponse authenticate(AuthRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String username = request.getUsername();

        User user = userRepo.findByUsername(username).orElseThrow(() -> new AppException("User not found.", HttpStatus.NOT_FOUND));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) throw new AppException("Invalid credentials", HttpStatus.FORBIDDEN);

        String accessToken = generateToken(user, accessTokenExpiration, "access");
        String refreshToken = generateToken(user, refreshTokenExpiration, "refresh");

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .authenticated(true)
                .build();
    }

    private String generateToken(User user, Long expirationTime, String tokenType) {
        try {
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issuer("https://api.tiemcheit.com")
                    .issueTime(Date.from(Instant.now()))
                    .expirationTime(Date.from(Instant.now().plus(expirationTime, ChronoUnit.SECONDS)))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("type", tokenType)
                    .claim("scope", buildScope(user))
                    .build();

            Payload payload = new Payload(jwtClaimsSet.toJSONObject());
            JWSObject jwsObject = new JWSObject(header, payload);
            jwsObject.sign(new MACSigner(secretKey.getBytes(StandardCharsets.UTF_8)));

            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Failed to create token for user: {}", user.getUsername(), e);
            throw new AuthenticationServiceException("Failed to generate token", e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }

    public AuthResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(request.getToken(), true);

        String jit = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

        invalidatedTokenRepo.save(invalidatedToken);

        String username = signedJWT.getJWTClaimsSet().getSubject();

        User user = userRepo.findByUsername(username).orElseThrow(() -> new AppException("User not found", HttpStatus.BAD_REQUEST));

        String accessToken = generateToken(user, accessTokenExpiration, "access");
        String refreshToken = generateToken(user, refreshTokenExpiration, "refresh");

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .authenticated(true)
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        SignedJWT signToken = verifyToken(request.getToken(), true);
        JWTClaimsSet claims = signToken.getJWTClaimsSet();

        String jit = claims.getJWTID();
        Date expiryTime = claims.getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

        invalidatedTokenRepo.save(invalidatedToken);
    }
}
