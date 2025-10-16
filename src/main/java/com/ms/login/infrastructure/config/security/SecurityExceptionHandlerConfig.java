package com.ms.login.infrastructure.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.login.entrypoint.controllers.handlers.ApiError;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
public class SecurityExceptionHandlerConfig {

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(ObjectMapper objectMapper) {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            ApiError error = new ApiError(401, "Unauthorized",
                    "Full authentication is required to access this resource",
                    request.getRequestURI());
            response.getWriter().write(objectMapper.writeValueAsString(error));
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(ObjectMapper objectMapper) {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            ApiError error = new ApiError(403, "Forbidden",
                    "You do not have permission to perform this action",
                    request.getRequestURI());
            response.getWriter().write(objectMapper.writeValueAsString(error));
        };
    }
}
