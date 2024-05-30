package com.tiemcheit.tiemcheitbe.config;

import com.tiemcheit.tiemcheitbe.model.Role;
import com.tiemcheit.tiemcheitbe.model.User;
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
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitConfig {
    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driver-class-name",
            havingValue = "com.mysql.cj.jdbc.Driver")

    public ApplicationRunner applicationRunner() {
        return args -> {
            List<String> roles = List.of("CUSTOMER", "EMPLOYEE", "ADMIN");
            roles.forEach(this::createRoleIfNotFound);

            userRepo.findByUsername("admin").ifPresentOrElse(user -> log.info("Admin user already exists: {}", user.getUsername()),
                    () -> {
                        User admin = User.builder().username("admin").password(passwordEncoder.encode("12345678")).fullname("Admin").phone("0917654321").email("admin@gmail.com").roles(Set.of(roleRepo.findByName("ADMIN").orElseThrow())).build();

                        userRepo.save(admin);
                        log.info("Created new admin user: {}", admin.getUsername());
                    }
            );
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
