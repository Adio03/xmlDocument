//package com.strata.xmlDocument.infrastructure.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
///**
// * Security configuration for the banking application
// * Configures authentication and authorization rules
// */
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(12); // Strong encryption for banking
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable()) // Disable CSRF for API-only application
//            .sessionManagement(session ->
//                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless sessions
//            .authorizeHttpRequests(auth -> auth
//                // Public endpoints
//                .requestMatchers("/api/v1/banking/users/register").permitAll()
//                .requestMatchers("/api/v1/banking/users/login").permitAll()
//                .requestMatchers("/actuator/health").permitAll()
//                .requestMatchers("/swagger-ui/**").permitAll()
//                .requestMatchers("/v3/api-docs/**").permitAll()
//                .requestMatchers("/api/v1/users/**").permitAll() // Legacy XML endpoints remain public
//                // All other banking endpoints require authentication
//                .requestMatchers("/api/v1/banking/**").authenticated()
//                .anyRequest().permitAll() // Allow other endpoints for existing functionality
//            );
//
//        return http.build();
//    }
//}