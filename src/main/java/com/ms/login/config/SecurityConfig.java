package com.ms.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorizedConfig -> {
            authorizedConfig.requestMatchers("/public").permitAll();
            authorizedConfig.requestMatchers("/swagger-ui**").permitAll();
            authorizedConfig.requestMatchers("/logout").permitAll();
            authorizedConfig.anyRequest().authenticated();
        })
        .oauth2Login(Customizer.withDefaults())
//        .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
        .build();
    }

}
