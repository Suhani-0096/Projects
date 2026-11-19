package BhagyaSwapnJewels.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration cors = new CorsConfiguration();

        cors.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://127.0.0.1:5173",
                "https://bhagyaswapnjewels.smarttechsolution.org"
        ));

        cors.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        cors.setAllowedHeaders(List.of("*"));
        cors.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source
                = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/api/**", cors);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            SecurityContextRepository securityContextRepository)
            throws Exception {

        http
                .cors(cors -> {
                })
                .csrf(csrf -> csrf.disable())
                .securityContext(context -> context
                        .securityContextRepository(securityContextRepository)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .authorizeHttpRequests(auth -> auth
                        // Public OPTIONS requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Public product browsing
                        .requestMatchers(HttpMethod.GET,
                                "/api/products", "/api/products/**").permitAll()
                        // Public Owner login
                        .requestMatchers("/api/owner/auth/login").permitAll()
                        // Owner authentication
                        .requestMatchers("/api/owner/auth/**").hasRole("OWNER")
                        // Public customer booking submission
                        .requestMatchers(HttpMethod.POST, "/api/bookings").permitAll()
                        // Owner-only notification APIs
                        .requestMatchers("/api/notifications/**").hasRole("OWNER")
                        // Owner-only booking management
                        .requestMatchers("/api/bookings/**").hasRole("OWNER")
                        // Public Manager login
                        .requestMatchers("/api/manager/auth/login").permitAll()
                        // All other Manager APIs
                        .requestMatchers("/api/manager/**").hasRole("MANAGER")
                        // Owner-only product modifications
                        .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("OWNER")
                        // Other Owner APIs
                        .requestMatchers("/api/owner/**").hasRole("OWNER")
                        // All remaining requests
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
