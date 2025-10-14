package com.ms.login.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //Responsavel por toda o controle do endpoints publicos e privados
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorizedConfig -> {
            authorizedConfig.requestMatchers("/public").permitAll();
            authorizedConfig.requestMatchers("/swagger-ui**").permitAll();
            authorizedConfig.requestMatchers("/logout").permitAll();
            authorizedConfig.anyRequest().authenticated();
        })
                .formLogin(form -> form.loginPage("/login").permitAll())
                .oauth2Login(Customizer.withDefaults())
                .logout(logout -> logout.permitAll())
//        .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
        .build();
    }

    //Responsavel por toda a autenticacao(ele decide ser o login e valido ou não)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
