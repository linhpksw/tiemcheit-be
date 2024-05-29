package com.tiemcheit.tiemcheitbe.config;

import com.tiemcheit.tiemcheitbe.model.Role;
import com.tiemcheit.tiemcheitbe.repository.RoleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitConfig {
    private final RoleRepo roleRepo;

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driver-class-name",
            havingValue = "com.mysql.cj.jdbc.Driver")

    public ApplicationRunner applicationRunner() {
        return args -> {
            List<String> roles = List.of("CUSTOMER", "EMPLOYEE", "ADMIN");
            roles.forEach(this::createRoleIfNotFound);
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
