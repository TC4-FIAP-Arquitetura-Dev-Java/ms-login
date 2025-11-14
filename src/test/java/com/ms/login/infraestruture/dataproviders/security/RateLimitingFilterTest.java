package com.ms.login.infraestruture.dataproviders.security;

import com.ms.login.infrastructure.security.RateLimitingFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RateLimitingFilterTest {

    @InjectMocks
    private RateLimitingFilter filter;

    @Mock
    private FilterChain filterChain;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void doFilterInternal_shouldAllowRequest_whenNotLoginEndpoint() throws ServletException, IOException {
        request.setRequestURI("/api/other");
        request.setMethod("GET");

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(response.getStatus()).isNotEqualTo(429);
    }

    @Test
    void doFilterInternal_shouldAllowRequest_whenLoginEndpointButNotLocked() throws ServletException, IOException {
        request.setRequestURI("/api/login");
        request.setMethod("POST");
        request.setRemoteAddr("192.168.1.1");

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldBlockRequest_whenIpIsLocked() throws ServletException, IOException {
        String clientIp = "192.168.1.100";
        request.setRequestURI("/api/login");
        request.setMethod("POST");
        request.setRemoteAddr(clientIp);

        // Registrar tentativas até bloquear
        for (int i = 0; i < 5; i++) {
            filter.recordFailedAttempt(clientIp);
        }

        filter.doFilter(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
        assertThat(response.getStatus()).isEqualTo(429);
        assertThat(response.getContentAsString()).contains("Too many login attempts");
    }

    @Test
    void recordFailedAttempt_shouldLockAfterMaxAttempts() {
        String clientIp = "192.168.1.50";

        for (int i = 0; i < 4; i++) {
            filter.recordFailedAttempt(clientIp);
        }

        // Ainda não deve estar bloqueado
        request.setRequestURI("/api/login");
        request.setMethod("POST");
        request.setRemoteAddr(clientIp);
        try {
            filter.doFilter(request, response, filterChain);
            assertThat(response.getStatus()).isNotEqualTo(429);
        } catch (Exception e) {
            // Ignorar
        }

        // 5ª tentativa deve bloquear
        filter.recordFailedAttempt(clientIp);

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        request.setRequestURI("/api/login");
        request.setMethod("POST");
        request.setRemoteAddr(clientIp);
        try {
            filter.doFilter(request, response, filterChain);
            assertThat(response.getStatus()).isEqualTo(429);
        } catch (Exception e) {
            // Ignorar
        }
    }

    @Test
    void clearAttempts_shouldRemoveLock() {
        String clientIp = "192.168.1.200";

        // Bloquear IP
        for (int i = 0; i < 5; i++) {
            filter.recordFailedAttempt(clientIp);
        }

        // Limpar tentativas
        filter.clearAttempts(clientIp);

        // Deve permitir requisições novamente
        request.setRequestURI("/api/login");
        request.setMethod("POST");
        request.setRemoteAddr(clientIp);
        try {
            filter.doFilter(request, response, filterChain);
            verify(filterChain).doFilter(request, response);
        } catch (Exception e) {
            // Ignorar
        }
    }

    @Test
    void doFilterInternal_shouldUseXForwardedFor_whenPresent() throws ServletException, IOException {
        String clientIp = "203.0.113.1";
        request.setRequestURI("/api/login");
        request.setMethod("POST");
        request.setRemoteAddr("127.0.0.1");
        request.addHeader("X-Forwarded-For", clientIp);

        // Bloquear usando o IP do header
        for (int i = 0; i < 5; i++) {
            filter.recordFailedAttempt(clientIp);
        }

        filter.doFilter(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
        assertThat(response.getStatus()).isEqualTo(429);
    }

    @Test
    void recordFailedAttempt_shouldNotLockBeforeMaxAttempts() throws ServletException, IOException {
        String clientIp = "192.168.1.75";

        // Registrar 4 tentativas (menos que o máximo)
        for (int i = 0; i < 4; i++) {
            filter.recordFailedAttempt(clientIp);
        }

        // Não deve estar bloqueado ainda
        request.setRequestURI("/api/login");
        request.setMethod("POST");
        request.setRemoteAddr(clientIp);
        filter.doFilter(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void clearAttempts_shouldAllowNewAttempts() throws ServletException, IOException {
        String clientIp = "192.168.1.300";

        // Bloquear
        for (int i = 0; i < 5; i++) {
            filter.recordFailedAttempt(clientIp);
        }

        // Limpar
        filter.clearAttempts(clientIp);

        // Deve permitir
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        request.setRequestURI("/api/login");
        request.setMethod("POST");
        request.setRemoteAddr(clientIp);
        filter.doFilter(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldAllowRequest_whenNotPostMethod() throws ServletException, IOException {
        request.setRequestURI("/api/login");
        request.setMethod("GET");

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldAllowRequest_whenUriDoesNotContainLogin() throws ServletException, IOException {
        request.setRequestURI("/api/other");
        request.setMethod("POST");

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
}

