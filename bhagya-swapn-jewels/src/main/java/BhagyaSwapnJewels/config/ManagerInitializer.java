package BhagyaSwapnJewels.config;

import BhagyaSwapnJewels.entity.Manager;
import BhagyaSwapnJewels.repository.ManagerRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ManagerInitializer {

    @Bean
    public CommandLineRunner createInitialManager(
            ManagerRepository managerRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.manager.username:}") String username,
            @Value("${app.manager.password:}") String password) {

        return args -> {

            if (username.isBlank() || password.isBlank()) {
                throw new IllegalStateException(
                        "Configure app.manager.username and app.manager.password first."
                );
            }

            Manager manager = managerRepository.findByUsername(username)
                    .orElse(null);

            if (manager == null) {

                manager = new Manager(
                        username,
                        passwordEncoder.encode(password),
                        "MANAGER"
                );

                managerRepository.save(manager);

                System.out.println("Initial Website Manager account created successfully.");

            } else {
                System.out.println("Website Manager account already exists.");
            }
        };
    }
}
