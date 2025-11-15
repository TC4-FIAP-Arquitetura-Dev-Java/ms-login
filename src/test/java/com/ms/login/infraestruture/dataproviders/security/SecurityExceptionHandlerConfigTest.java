package com.ms.login.infraestruture.dataproviders.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.login.infrastructure.security.SecurityExceptionHandlerConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityExceptionHandlerConfigTest {

    private SecurityExceptionHandlerConfig config;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        config = new SecurityExceptionHandlerConfig();
        objectMapper = new ObjectMapper();
    }

    @Test
    void authenticationEntryPoint_shouldSetUnauthorizedStatus() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/protected");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = new AuthenticationException("Unauthorized") {};

        var entryPoint = config.authenticationEntryPoint(objectMapper);
        entryPoint.commence(request, response, authException);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(response.getContentType()).isEqualTo("application/json");
    }

    @Test
    void authenticationEntryPoint_shouldWriteErrorJson() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/protected");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = new AuthenticationException("Unauthorized") {};

        var entryPoint = config.authenticationEntryPoint(objectMapper);
        entryPoint.commence(request, response, authException);

        String content = response.getContentAsString();
        assertThat(content).contains("\"status\":401");
        assertThat(content).contains("\"error\":\"Unauthorized\"");
        assertThat(content).contains("\"path\":\"/api/protected\"");
    }

    @Test
    void accessDeniedHandler_shouldSetForbiddenStatus() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/admin");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AccessDeniedException accessDeniedException = new AccessDeniedException("Access denied");

        var handler = config.accessDeniedHandler(objectMapper);
        handler.handle(request, response, accessDeniedException);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_FORBIDDEN);
        assertThat(response.getContentType()).isEqualTo("application/json");
    }

    @Test
    void accessDeniedHandler_shouldWriteErrorJson() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/admin");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AccessDeniedException accessDeniedException = new AccessDeniedException("Access denied");

        var handler = config.accessDeniedHandler(objectMapper);
        handler.handle(request, response, accessDeniedException);

        String content = response.getContentAsString();
        assertThat(content).contains("\"status\":403");
        assertThat(content).contains("\"error\":\"Forbidden\"");
        assertThat(content).contains("\"path\":\"/api/admin\"");
    }
}

