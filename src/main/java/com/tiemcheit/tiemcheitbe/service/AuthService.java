package com.tiemcheit.tiemcheitbe.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.tiemcheit.tiemcheitbe.dto.request.*;
import com.tiemcheit.tiemcheitbe.dto.response.AuthResponse;
import com.tiemcheit.tiemcheitbe.dto.response.ExchangeTokenResponse;
import com.tiemcheit.tiemcheitbe.dto.response.GoogleUserResponse;
import com.tiemcheit.tiemcheitbe.dto.response.UserInfoResponse;
import com.tiemcheit.tiemcheitbe.exception.AppException;
import com.tiemcheit.tiemcheitbe.mapper.UserMapper;
import com.tiemcheit.tiemcheitbe.model.*;
import com.tiemcheit.tiemcheitbe.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final ActiveRefreshTokenRepo activeRefreshTokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;
    private final VerificationCodeRepo verificationCodeRepo;
    private final UserMapper userMapper;
    private final VerificationService verificationService;
    private final OAuth2Client oAuth2Client;
    private final UserClient userClient;

    @Value("${oauth2.client-id}")
    private String clientId;

    @Value("${oauth2.client-secret}")
    private String clientSecret;

    @Value("${oauth2.redirect-uri}")
    private String redirectUri;

    @Value("${oauth2.grant-type}")
    private String grantType;

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long accessTokenExpiration;

    @Value("${security.jwt.refresh-token-expiration-time}")
    private long refreshTokenExpiration;

    public AuthResponse oauth2(String code) throws ParseException {
        ExchangeTokenRequest request = ExchangeTokenRequest.builder()
                .code(code)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(grantType)
                .redirectUri(redirectUri)
                .build();

        ExchangeTokenResponse response = oAuth2Client.exchangeToken(request);

//        log.info("Response: {}", response);

        String token = response.getAccessToken();
        GoogleUserResponse googleUserInfo = userClient.getGoogleUserInfo("json", token);

//        log.info("Google user info: {}", googleUserInfo);

        User user = userRepo.findByEmail(googleUserInfo.getEmail()).orElseGet(() -> {
            String baseUsername = googleUserInfo.getEmail().split("@")[0];
            baseUsername = baseUsername.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

            String username = generateUniqueUsername(baseUsername);

            User newUser = User.builder()
                    .username(username)
                    .email(googleUserInfo.getEmail())
                    .fullname(googleUserInfo.getName())
                    .isActivated(true)
                    .build();

            HashSet<Role> roles = new HashSet<>();
            roleRepo.findByName("CUSTOMER").ifPresent(roles::add);
            newUser.setRoles(roles);

            return userRepo.save(newUser);
        });

        user.setIsActivated(true);

        if ("DELETED".equals(user.getStatus())) {
            throw new AppException("This account has been deleted.", HttpStatus.FORBIDDEN);
        }

        return getAuthResponse(user);
    }

    private String generateUniqueUsername(String baseUsername) {
        String username = baseUsername;
        int counter = 0;
        while (userRepo.existsByUsername(username)) {
            counter++;
            username = baseUsername + counter;
        }
        return username;
    }

    @Transactional
    public UserInfoResponse register(UserRegisterRequest request) {
        User existingUser = userRepo.findByUsername(request.getUsername()).orElse(null);
        if (existingUser != null) {
            if (!"DELETED".equals(existingUser.getStatus())) {
                throw new AppException("User already exists with this username", HttpStatus.BAD_REQUEST);
            } else {
                throw new AppException("This account has been deleted.", HttpStatus.FORBIDDEN);
            }
        }

        if (userRepo.existsByEmail(request.getEmail())) {
            throw new AppException("User already exists with this email", HttpStatus.BAD_REQUEST);
        }

        if (userRepo.existsByPhone(request.getPhone())) {
            throw new AppException("User already exists with this phone", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepo.findByName("CUSTOMER").ifPresent(roles::add);
        user.setRoles(roles);

        if (request.getAddresses() != null) {
            User finalUser = user;
            Set<UserAddress> addresses = request.getAddresses().stream()
                    .map(addr -> UserAddress.builder()
                            .address(addr.getAddress())
                            .isDefault(addr.getIsDefault())
                            .user(finalUser)
                            .build())
                    .collect(Collectors.toSet());

            user.setAddresses(addresses);
        }

        List<VerificationCode> verificationCodes = new ArrayList<>();
        verificationCodes.add(verificationService.generateVerificationCode(user));
        user.setVerificationCodes(verificationCodes);

        verificationService.sendVerificationCode(user.getEmail(), verificationCodes.getFirst().getCode());

        user = userRepo.save(user);

        return userMapper.toUserInfoResponse(user);
    }

    public void introspect(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        verifyToken(token, false);
    }

    public SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException {
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

            if (activeRefreshTokenRepo.findByJti(jti).isEmpty()) {
                throw new AppException(STR."Refresh token \{token} not found in repository.", HttpStatus.UNAUTHORIZED);
            }
        }

        boolean verified = signedJWT.verify(verifier);

        if (!verified) {
            throw new AppException("Token is not verified", HttpStatus.UNAUTHORIZED);
        }

        if (!expiryTime.after(new Date())) {
            throw new AppException("Token is expired", HttpStatus.UNAUTHORIZED);
        }

        return signedJWT;
    }

    public AuthResponse authenticate(AuthRequest request) throws ParseException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String credential = request.getCredential();

        User user = userRepo.findByCredential(credential).orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));


        if ("DELETED".equals(user.getStatus())) {
            throw new AppException("This account has been deleted", HttpStatus.FORBIDDEN);
        }

        if (!user.getIsActivated()) {
            throw new AppException(STR."This account has not been activated. Please enter verification code sent to the email \{user.getEmail()}", HttpStatus.FORBIDDEN);
        }

        if (user.getPassword() == null) {
            throw new AppException("This account doesn't register password. Please login using Google and update your password in Profile page", HttpStatus.FORBIDDEN);
        }

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppException("Password is not correct", HttpStatus.FORBIDDEN);
        }
        activeRefreshTokenRepo.deleteByUser_Username(user.getUsername());

        return getAuthResponse(user);
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
                stringJoiner.add(STR."ROLE_\{role.getName()}");
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }

    public AuthResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(request.getToken(), true);

        String jti = signedJWT.getJWTClaimsSet().getJWTID();
        activeRefreshTokenRepo.deleteByJti(jti);

        String username = signedJWT.getJWTClaimsSet().getSubject();

        User user = userRepo.findByUsername(username).orElseThrow(() -> new AppException("User not found", HttpStatus.BAD_REQUEST));

        return getAuthResponse(user);
    }

    private AuthResponse getAuthResponse(User user) throws ParseException {
        String accessToken = generateToken(user, accessTokenExpiration, "access");
        String refreshToken = generateToken(user, refreshTokenExpiration, "refresh");

        String newJti = extractJtiFromToken(refreshToken);
        activeRefreshTokenRepo.save(ActiveRefreshToken.builder().jti(newJti).user(user).build());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        SignedJWT signToken = verifyToken(request.getToken(), true);
        JWTClaimsSet claims = signToken.getJWTClaimsSet();

        String jit = claims.getJWTID();
        activeRefreshTokenRepo.deleteByJti(jit);
    }

    private String extractJtiFromToken(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet().getJWTID();
    }

    public void changePassword(String username, String currentPassword, String newPassword, Boolean isHavePassword) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        if (isHavePassword && !passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new AppException("Current password is not correct", HttpStatus.FORBIDDEN);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }

    public void resetPassword(String email, String newPassword, String code) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        boolean codeFound = user.getVerificationCodes().stream()
                .anyMatch(vc -> vc.getCode().equals(code) && !vc.isExpired());

        log.info("Code found: {}", codeFound);

        if (!codeFound) {
            throw new AppException("Verification code not found or expired", HttpStatus.BAD_REQUEST);
        }
        
        user.getVerificationCodes().clear();
        verificationCodeRepo.deleteByUserId(user.getId());

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }

    public void deactivate(DeactivateRequest request) throws ParseException, JOSEException {
        String username = request.getUsername();

        User user = userRepo.findByUsername(username).orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new AppException("Current password is not correct", HttpStatus.FORBIDDEN);
        }

        user.setStatus("DELETED");
        userRepo.save(user);

        SignedJWT signToken = verifyToken(request.getToken(), true);
        JWTClaimsSet claims = signToken.getJWTClaimsSet();

        String jit = claims.getJWTID();
        activeRefreshTokenRepo.deleteByJti(jit);
    }


    public void sendForgotCode(ForgotPasswordRequest request) {
        String email = request.getEmail();

        User user = userRepo.findByEmail(email).orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        List<VerificationCode> verificationCodes = new ArrayList<>();
        verificationCodes.add(verificationService.generateVerificationCode(user));
        user.setVerificationCodes(verificationCodes);

        verificationService.sendVerificationCode(user.getEmail(), verificationCodes.getFirst().getCode());

        userRepo.save(user);
    }


}
