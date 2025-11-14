package com.ms.login.infraestruture.dataproviders.security;

import com.ms.login.infrastructure.security.InputValidationFilter;
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
class InputValidationFilterTest {

    @InjectMocks
    private InputValidationFilter filter;

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
    void doFilterInternal_shouldAllowValidRequest() throws ServletException, IOException {
        request.setRequestURI("/api/login");
        request.setMethod("POST");
        request.addParameter("username", "validuser");
        request.addParameter("password", "validpass123");

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(response.getStatus()).isNotEqualTo(400);
    }

    @Test
    void doFilterInternal_shouldBlockSqlInjectionInQueryString() throws ServletException, IOException {
        request.setRequestURI("/api/login");
        request.setQueryString("username=admin' OR '1'='1");

        filter.doFilter(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).contains("Invalid input detected");
    }

    @Test
    void doFilterInternal_shouldBlockXssInQueryString() throws ServletException, IOException {
        request.setRequestURI("/api/login");
        request.setQueryString("username=<script>alert('xss')</script>");

        filter.doFilter(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    void doFilterInternal_shouldBlockSqlInjectionInParameters() throws ServletException, IOException {
        request.setRequestURI("/api/login");
        request.addParameter("username", "admin'; DROP TABLE users; --");

        filter.doFilter(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    void doFilterInternal_shouldBlockXssInParameters() throws ServletException, IOException {
        request.setRequestURI("/api/login");
        request.addParameter("username", "<iframe src='evil.com'></iframe>");

        filter.doFilter(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    void doFilterInternal_shouldBlockSuspiciousUserAgent() throws ServletException, IOException {
        request.setRequestURI("/api/login");
        StringBuilder longUserAgent = new StringBuilder();
        for (int i = 0; i < 501; i++) {
            longUserAgent.append("a");
        }
        request.addHeader("User-Agent", longUserAgent.toString());

        filter.doFilter(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).contains("Suspicious request detected");
    }

    @Test
    void doFilterInternal_shouldBlockXssInUserAgent() throws ServletException, IOException {
        request.setRequestURI("/api/login");
        request.addHeader("User-Agent", "<script>alert('xss')</script>");

        filter.doFilter(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    void doFilterInternal_shouldBlockSuspiciousXForwardedForHeader() throws ServletException, IOException {
        request.setRequestURI("/api/login");
        StringBuilder longHeader = new StringBuilder();
        for (int i = 0; i < 101; i++) {
            longHeader.append("a");
        }
        request.addHeader("X-Forwarded-For", longHeader.toString());

        filter.doFilter(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    void isValidEmail_shouldReturnTrueForValidEmail() {
        assertThat(InputValidationFilter.isValidEmail("user@example.com")).isTrue();
        assertThat(InputValidationFilter.isValidEmail("test.user+tag@domain.co.uk")).isTrue();
    }

    @Test
    void isValidEmail_shouldReturnFalseForInvalidEmail() {
        assertThat(InputValidationFilter.isValidEmail("invalid-email")).isFalse();
        assertThat(InputValidationFilter.isValidEmail("@example.com")).isFalse();
        assertThat(InputValidationFilter.isValidEmail("user@")).isFalse();
        assertThat(InputValidationFilter.isValidEmail(null)).isFalse();
    }

    @Test
    void isValidUsername_shouldReturnTrueForValidUsername() {
        assertThat(InputValidationFilter.isValidUsername("user123")).isTrue();
        assertThat(InputValidationFilter.isValidUsername("test_user")).isTrue();
        assertThat(InputValidationFilter.isValidUsername("user-name")).isTrue();
    }

    @Test
    void isValidUsername_shouldReturnFalseForInvalidUsername() {
        assertThat(InputValidationFilter.isValidUsername("ab")).isFalse(); // muito curto
        assertThat(InputValidationFilter.isValidUsername("a".repeat(21))).isFalse(); // muito longo
        assertThat(InputValidationFilter.isValidUsername("user@name")).isFalse(); // caractere inválido
        assertThat(InputValidationFilter.isValidUsername(null)).isFalse();
    }

    @Test
    void containsMaliciousContent_shouldDetectSqlInjection() {
        assertThat(InputValidationFilter.containsMaliciousContent("admin' OR '1'='1")).isTrue();
        assertThat(InputValidationFilter.containsMaliciousContent("'; DROP TABLE users; --")).isTrue();
        assertThat(InputValidationFilter.containsMaliciousContent("UNION SELECT * FROM users")).isTrue();
    }

    @Test
    void containsMaliciousContent_shouldDetectXss() {
        assertThat(InputValidationFilter.containsMaliciousContent("<script>alert('xss')</script>")).isTrue();
        assertThat(InputValidationFilter.containsMaliciousContent("javascript:alert('xss')")).isTrue();
        assertThat(InputValidationFilter.containsMaliciousContent("<iframe src='evil.com'></iframe>")).isTrue();
    }

    @Test
    void containsMaliciousContent_shouldReturnFalseForSafeContent() {
        assertThat(InputValidationFilter.containsMaliciousContent("normal text")).isFalse();
        assertThat(InputValidationFilter.containsMaliciousContent("user@example.com")).isFalse();
        assertThat(InputValidationFilter.containsMaliciousContent(null)).isFalse();
    }

    @Test
    void doFilterInternal_shouldAllowRequest_whenNoMaliciousContent() throws ServletException, IOException {
        request.setRequestURI("/api/login");
        request.setMethod("POST");
        request.addParameter("username", "validuser123");
        request.addParameter("password", "validpass");

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(response.getStatus()).isNotEqualTo(400);
    }

    @Test
    void doFilterInternal_shouldBlock_whenXRealIpHeaderIsSuspicious() throws ServletException, IOException {
        request.setRequestURI("/api/login");
        request.addHeader("X-Real-IP", "'; DROP TABLE users; --");

        filter.doFilter(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    void doFilterInternal_shouldBlock_whenXOriginatingIpHeaderIsSuspicious() throws ServletException, IOException {
        request.setRequestURI("/api/login");
        request.addHeader("X-Originating-IP", "<script>alert('xss')</script>");

        filter.doFilter(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    void doFilterInternal_shouldBlock_whenXForwardedForHeaderIsTooLong() throws ServletException, IOException {
        request.setRequestURI("/api/login");
        StringBuilder longHeader = new StringBuilder();
        for (int i = 0; i < 101; i++) {
            longHeader.append("a");
        }
        request.addHeader("X-Forwarded-For", longHeader.toString());

        filter.doFilter(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    void doFilterInternal_shouldAllowRequest_whenQueryStringIsNull() throws ServletException, IOException {
        request.setRequestURI("/api/login");
        request.setQueryString(null);
        request.addParameter("username", "validuser");

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldAllowRequest_whenUserAgentIsNull() throws ServletException, IOException {
        request.setRequestURI("/api/login");
        request.addHeader("User-Agent", null);

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldAllowRequest_whenUserAgentIsNormalLength() throws ServletException, IOException {
        request.setRequestURI("/api/login");
        request.addHeader("User-Agent", "Mozilla/5.0");

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldAllowRequest_whenSuspiciousHeaderIsNull() throws ServletException, IOException {
        request.setRequestURI("/api/login");
        request.addHeader("X-Forwarded-For", null);

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldAllowRequest_whenSuspiciousHeaderIsNormalLength() throws ServletException, IOException {
        request.setRequestURI("/api/login");
        request.addHeader("X-Forwarded-For", "192.168.1.1");

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
}

