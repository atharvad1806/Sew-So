package com.athdhup.sewnso.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // TODO
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // TEMPORARY - everything open until JWT auth is built
            );

        return http.build();
    }
}