package com.projetointegrador.comunicavet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configure(http))
                .csrf(csrf -> csrf.disable()) // API REST não usa CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // permite tudo por enquanto
                );

        return http.build();
    }
}
