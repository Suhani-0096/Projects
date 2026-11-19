package BhagyaSwapnJewels.config;

import BhagyaSwapnJewels.entity.Owner;
import BhagyaSwapnJewels.repository.OwnerRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class OwnerInitializer {

    @Bean
    public CommandLineRunner createInitialOwner(
            OwnerRepository ownerRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.owner.username:}") String username,
            @Value("${app.owner.password:}") String password) {

        return args -> {

            if (username.isBlank() || password.isBlank()) {
                throw new IllegalStateException(
                        "Configure app.owner.username and app.owner.password first."
                );
            }

            Owner owner = ownerRepository.findByUsername(username)
                    .orElse(null);

            if (owner == null) {
                owner = new Owner(
                        username,
                        passwordEncoder.encode(password),
                        "OWNER"
                );
            } else {
                // Reset the existing owner's password
                owner.setPassword(passwordEncoder.encode(password));
            }

            ownerRepository.save(owner);

            System.out.println(
                    "Owner account created or password reset successfully."
            );

            System.out.println(
                    "Password verification: "
                    + passwordEncoder.matches(password, owner.getPassword())
            );
        };
    }
}
