package BhagyaSwapnJewels.config;

import BhagyaSwapnJewels.service.ManagerDetailsService;
import BhagyaSwapnJewels.service.OwnerDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthenticationProviderConfig {

    @Bean
    @Primary
    @Qualifier("ownerAuthenticationManager")
    public AuthenticationManager ownerAuthenticationManager(
            OwnerDetailsService ownerDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider
                = new DaoAuthenticationProvider(ownerDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(provider);
    }

    @Bean
    @Qualifier("managerAuthenticationManager")
    public AuthenticationManager managerAuthenticationManager(
            ManagerDetailsService managerDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider
                = new DaoAuthenticationProvider(managerDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(provider);
    }
}
