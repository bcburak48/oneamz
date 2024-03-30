package com.oneamz.inventory.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.debug("Configuring HttpSecurity...");
        http
                .csrf(AbstractHttpConfigurer::disable)  // Disable CSRF
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/products/**").authenticated()  // Require authentication for product endpoints
                        .anyRequest().permitAll())  // Allow all other requests
                .httpBasic(withDefaults());  // Use withDefaults() to configure HTTP Basic authentication
        log.debug("HttpSecurity configuration complete.");
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.debug("Configuring BCryptPasswordEncoder...");
        return new BCryptPasswordEncoder();
    }
}
