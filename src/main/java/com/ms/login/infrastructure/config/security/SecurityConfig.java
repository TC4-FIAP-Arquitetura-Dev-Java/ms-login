package com.ms.login.infrastructure.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    //Responsavel por toda o controle do endpoints publicos e privados
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizedConfig -> {
            authorizedConfig.requestMatchers("/public").permitAll();
            authorizedConfig.requestMatchers("/swagger-ui**").permitAll();
            authorizedConfig.requestMatchers("/logout").permitAll();
            authorizedConfig.anyRequest().authenticated();
        })
                //TODO: aJUSTAR O SECURITY CONFIG PARA TER SUPORTE PARA OAUTH E JWT NORMAL
//                .formLogin(form -> form.loginPage("/login").permitAll())
//                .oauth2Login(Customizer.withDefaults())
//                .logout(logout -> logout.permitAll())

//        .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
    }

    //Responsavel por toda a autenticacao(ele decide ser o login e valido ou não)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
