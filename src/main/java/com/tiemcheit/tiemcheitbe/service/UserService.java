package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.*;
import com.tiemcheit.tiemcheitbe.dto.response.UserAddAddressResponse;
import com.tiemcheit.tiemcheitbe.dto.response.UserAvatarResponse;
import com.tiemcheit.tiemcheitbe.dto.response.UserInfoResponse;
import com.tiemcheit.tiemcheitbe.dto.response.UserProfileResponse;
import com.tiemcheit.tiemcheitbe.mapper.UserAddressMapper;
import com.tiemcheit.tiemcheitbe.mapper.UserAvatarMapper;
import com.tiemcheit.tiemcheitbe.mapper.UserMapper;
import com.tiemcheit.tiemcheitbe.model.*;
import com.tiemcheit.tiemcheitbe.repository.*;
import com.tiemcheit.tiemcheitbe.repository.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepo userRepo;
    private final UserAvatarRepo userAvatarRepo;
    private final RoleRepo roleRepo;
    private final UserAddressRepo userAddressRepo;
    private final PermissionRepo permissionRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserAddressMapper userAddressMapper;
    private final UserAvatarMapper userAvatarMapper;

    @PreAuthorize("#username == authentication.name || hasRole('ROLE_ADMIN')")
    public UserInfoResponse getUserInfo(String username) {
        return userMapper.toUserInfoResponse(
                userRepo.findByUsername(username).orElseThrow(() -> new AppException("User not found.", HttpStatus.NOT_FOUND)));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserInfoResponse> getUsersInfo() {
        return userRepo.findAll().stream().map(userMapper::toUserInfoResponse).toList();
    }


    @PreAuthorize("#username == authentication.name || hasRole('ROLE_ADMIN')")
    public UserProfileResponse updateUserProfile(String username, UserUpdateRequest request) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getFullname() != null) {
            user.setFullname(request.getFullname());
        }

        if (request.getPhone() != null && !request.getPhone().equals(user.getPhone())) {
            if (userRepo.existsByPhoneAndIdNot(request.getPhone(), user.getId())) {
                throw new AppException("User already exists with this phone", HttpStatus.BAD_REQUEST);
            }
            user.setPhone(request.getPhone());
        }

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepo.existsByEmailAndIdNot(request.getEmail(), user.getId())) {
                throw new AppException("User already exists with this email", HttpStatus.BAD_REQUEST);
            }
            user.setEmail(request.getEmail());
        }

        if (request.getDob() != null) {
            user.setDob(request.getDob());
        }

        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }

        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }

        if (request.getIsActivated() != null) {
            user.setIsActivated(request.getIsActivated());
        }

        if (request.getRoles() != null) {
            user.getRoles().clear();

            for (RoleRequest roleRequest : request.getRoles()) {
                Role role = roleRepo.findByName(roleRequest.getName()).orElseThrow(() -> new AppException("Role not found.", HttpStatus.NOT_FOUND));

                Set<Permission> permissions = new HashSet<>();
                for (String permName : roleRequest.getPermissions()) {
                    Permission permission = permissionRepo.findByName(permName)
                            .orElseThrow(() -> new AppException("Permission not found.", HttpStatus.NOT_FOUND));
                    permissions.add(permission);
                }
                role.setPermissions(permissions);

                user.getRoles().add(role);
            }
        }

        User savedUser = userRepo.save(user);
        return userMapper.toUserProfileResponse(savedUser);
    }

    @PreAuthorize("#username == authentication.name || hasRole('ROLE_ADMIN')")
    public UserProfileResponse getUserProfile(String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new AppException("User not found.", HttpStatus.NOT_FOUND));

        return userMapper.toUserProfileResponse(user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserProfileResponse> getUsersProfile() {
        return userRepo.findAll().stream().map(userMapper::toUserProfileResponse).toList();
    }

    @PreAuthorize("#username == authentication.name || hasRole('ROLE_ADMIN')")
    public void updateUserAddress(String username, Long addressId, UserUpdateAddressRequest request) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new AppException("User not found.", HttpStatus.NOT_FOUND));

        if (userAddressRepo.existsByAddressAndUserId(request.getAddress(), user.getId()) && request.getType().equals("address")) {
            throw new AppException("User already exists with this address", HttpStatus.BAD_REQUEST);
        }

        List<UserAddress> addresses = userAddressRepo.findAllByUserId(user.getId());

        addresses.forEach(addr -> addr.setIsDefault(false));

        // Find the address to update and set it to the request's default status if true
        UserAddress userAddress = addresses.stream()
                .filter(addr -> addr.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new AppException("Address not found.", HttpStatus.NOT_FOUND));

        userAddress.setAddress(request.getAddress());

        if (request.getIsDefault()) {
            userAddress.setIsDefault(true);
        }

        // Save all the addresses back to the database
        userAddressRepo.saveAll(addresses);
    }

    @PreAuthorize("#username == authentication.name || hasRole('ROLE_ADMIN')")
    public UserAddAddressResponse addUserAddress(String username, UserAddAddressRequest request) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new AppException("User not found.", HttpStatus.NOT_FOUND));


        if (userAddressRepo.existsByAddressAndUserId(request.getAddress(), user.getId())) {
            throw new AppException("User already exists with this address", HttpStatus.BAD_REQUEST);
        }

        UserAddress userAddress = userAddressRepo.save(
                UserAddress.builder()
                        .address(request.getAddress())
                        .isDefault(request.getIsDefault())
                        .user(user)
                        .build());

        return userAddressMapper.toUserAddAddressResponse(userAddress);
    }

    @Transactional
    @PreAuthorize("#username == authentication.name || hasRole('ROLE_ADMIN')")
    public void deleteUserAddress(String username, Long addressId) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new AppException("User not found.", HttpStatus.NOT_FOUND));

        userAddressRepo.findById(addressId)
                .orElseThrow(() -> new AppException("Address not found.", HttpStatus.NOT_FOUND));

        // Delete the address
        userAddressRepo.deleteByIdAndUserId(addressId, user.getId());
    }

    public UserAvatarResponse getUserAvatar(String username) {
        UserAvatar userAvatar = userAvatarRepo.findByUser_Username(username);

        return userAvatarMapper.toUserAvatarResponse(userAvatar);
    }

    public UserAvatarResponse addUserAvatar(String username, UserAvatarRequest request) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new AppException("User not found.", HttpStatus.NOT_FOUND));

        String image = request.getImage();

        log.info("Image: {}", request.getImage());

        UserAvatar userAvatar = userAvatarRepo.findByUser_Username(username);

        if (userAvatar == null) {
            userAvatar = userAvatarRepo.save(UserAvatar.builder().image(image).user(user).build());
        } else {
            userAvatar.setImage(image);
            userAvatarRepo.save(userAvatar);
        }

        return userAvatarMapper.toUserAvatarResponse(userAvatar);
    }
}
