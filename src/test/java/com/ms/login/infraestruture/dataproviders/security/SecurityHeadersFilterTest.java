package com.ms.login.infraestruture.dataproviders.security;

import com.ms.login.infrastructure.security.SecurityHeadersFilter;
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
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SecurityHeadersFilterTest {

    @InjectMocks
    private SecurityHeadersFilter filter;

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
    void doFilterInternal_shouldSetSecurityHeaders() throws ServletException, IOException {
        request.setRequestURI("/api/test");

        filter.doFilter(request, response, filterChain);

        assertThat(response.getHeader("X-Frame-Options")).isEqualTo("DENY");
        assertThat(response.getHeader("X-Content-Type-Options")).isEqualTo("nosniff");
        assertThat(response.getHeader("X-XSS-Protection")).isEqualTo("1; mode=block");
        assertThat(response.getHeader("Referrer-Policy")).isEqualTo("strict-origin-when-cross-origin");
        assertThat(response.getHeader("Content-Security-Policy")).isNotNull();
        assertThat(response.getHeader("Permissions-Policy")).isNotNull();
        
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldSetHstsHeader_whenRequestIsSecure() throws ServletException, IOException {
        request.setRequestURI("/api/test");
        request.setSecure(true);

        filter.doFilter(request, response, filterChain);

        assertThat(response.getHeader("Strict-Transport-Security"))
            .isEqualTo("max-age=31536000; includeSubDomains; preload");
    }

    @Test
    void doFilterInternal_shouldNotSetHstsHeader_whenRequestIsNotSecure() throws ServletException, IOException {
        request.setRequestURI("/api/test");
        request.setSecure(false);

        filter.doFilter(request, response, filterChain);

        assertThat(response.getHeader("Strict-Transport-Security")).isNull();
    }

    @Test
    void doFilterInternal_shouldSetCacheControlForSensitiveEndpoints() throws ServletException, IOException {
        request.setRequestURI("/api/login");
        request.setMethod("POST");

        filter.doFilter(request, response, filterChain);

        assertThat(response.getHeader("Cache-Control"))
            .isEqualTo("no-store, no-cache, must-revalidate, private");
        assertThat(response.getHeader("Pragma")).isEqualTo("no-cache");
        assertThat(response.getHeader("Expires")).isEqualTo("0");
    }

    @Test
    void doFilterInternal_shouldSetCacheControlForUsersEndpoint() throws ServletException, IOException {
        request.setRequestURI("/api/users");
        request.setMethod("GET");

        filter.doFilter(request, response, filterChain);

        assertThat(response.getHeader("Cache-Control"))
            .isEqualTo("no-store, no-cache, must-revalidate, private");
    }

    @Test
    void doFilterInternal_shouldSetCacheControlForAuthEndpoint() throws ServletException, IOException {
        request.setRequestURI("/api/auth");
        request.setMethod("GET");

        filter.doFilter(request, response, filterChain);

        assertThat(response.getHeader("Cache-Control"))
            .isEqualTo("no-store, no-cache, must-revalidate, private");
    }

    @Test
    void doFilterInternal_shouldSetCacheControlForAdminEndpoint() throws ServletException, IOException {
        request.setRequestURI("/api/admin");
        request.setMethod("GET");

        filter.doFilter(request, response, filterChain);

        assertThat(response.getHeader("Cache-Control"))
            .isEqualTo("no-store, no-cache, must-revalidate, private");
    }

    @Test
    void doFilterInternal_shouldNotSetCacheControlForNonSensitiveEndpoints() throws ServletException, IOException {
        request.setRequestURI("/api/public");
        request.setMethod("GET");

        filter.doFilter(request, response, filterChain);

        assertThat(response.getHeader("Cache-Control")).isNull();
        assertThat(response.getHeader("Pragma")).isNull();
        assertThat(response.getHeader("Expires")).isNull();
    }

}

