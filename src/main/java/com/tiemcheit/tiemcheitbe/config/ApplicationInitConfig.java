package com.tiemcheit.tiemcheitbe.config;

import com.tiemcheit.tiemcheitbe.model.Role;
import com.tiemcheit.tiemcheitbe.repository.RoleRepo;
import com.tiemcheit.tiemcheitbe.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitConfig {
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driver-class-name",
            havingValue = "com.mysql.cj.jdbc.Driver")

    public ApplicationRunner applicationRunner(UserRepo userRepo, RoleRepo roleRepo) {
        return args -> {
            List<String> roles = List.of("CUSTOMER", "EMPLOYEE", "ADMIN");
            roles.forEach(this::createRoleIfNotFound);

//            Role adminRole = Role.builder()
//                    .name("ADMIN")
//                    .description("Admin role")
//                    .build();
//
//            var customRole = new HashSet<Role>();
//            customRole.add(adminRole);
//
//            User user = User.builder()
//                    .username("admin")
//                    .password(passwordEncoder.encode("admin"))
//                    .email("admin@gmail.com")
//                    .phone("0375830815")
//                    .fullname("Admin")
//                    .roles(customRole)
//                    .build();
//
//            userRepo.save(user);
//            log.warn("admin user has been created with default password: admin, please change it");
        };
    }

    private void createRoleIfNotFound(String roleName) {
        roleRepo.findByName(roleName).ifPresentOrElse(
                role -> log.info("Role already exists: {}", role.getName()),
                () -> {
                    Role newRole = Role.builder()
                            .name(roleName)
                            .description(roleName + " role")
                            .build();
                    roleRepo.save(newRole);
                    log.info("Created new role: {}", roleName);
                }
        );
    }
}
