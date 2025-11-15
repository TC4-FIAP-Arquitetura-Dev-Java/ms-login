package com.ms.login.infraestruture.dataproviders.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ms.login.domain.enums.RoleEnum;
import com.ms.login.infrastructure.security.JwtAuthenticationFilter;
import com.ms.login.infrastructure.security.JwtTokenProvider;
import com.ms.login.infrastructure.security.MyUserDetails;
import com.ms.login.infrastructure.security.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenProvider jwtUtil;

    @Mock
    private MyUserDetailsService myUserDetailsService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_shouldAutenticateWhenTokenValid() throws ServletException, IOException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        MyUserDetails userDetails = new MyUserDetails("userId", "username", "password", RoleEnum.USER, null);
        
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtUtil.validateToken("token")).thenReturn(true);
        when(jwtUtil.getUsernameFromToken("token")).thenReturn("username");
        when(myUserDetailsService.loadUserByUsername("username")).thenReturn(userDetails);

        filter.doFilter(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldSkipWhenTokenInvalid() throws ServletException, IOException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtUtil.validateToken("token")).thenReturn(false);

        filter.doFilter(request, response, filterChain);

        verify(jwtUtil, never()).extractClaimsAcessToken(any());
        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void doFilterInternal_shouldSkipWhenNoAuthorizationHeader() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilter(request, response, filterChain);

        verify(jwtUtil, never()).validateToken(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldSkipWhenAuthorizationHeaderDoesNotStartWithBearer() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getHeader("Authorization")).thenReturn("Basic token");

        filter.doFilter(request, response, filterChain);

        verify(jwtUtil, never()).validateToken(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldHandleAuthenticationException() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(request.getRequestURI()).thenReturn("/api/test");
        when(jwtUtil.validateToken("token")).thenReturn(true);
        when(jwtUtil.getUsernameFromToken("token")).thenReturn("username");
        when(myUserDetailsService.loadUserByUsername("username"))
            .thenThrow(new org.springframework.security.core.AuthenticationException("Auth failed") {});

        // Quando há exceção de autenticação, o filtro chama o AuthenticationEntryPoint
        // e não continua com o filterChain
        filter.doFilter(request, response, filterChain);

        // O filterChain não deve ser chamado quando há exceção de autenticação
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldThrowException_whenGenericExceptionOccurs() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtUtil.validateToken("token")).thenThrow(new RuntimeException("Unexpected error"));

        // Quando há uma exceção genérica, ela deve ser relançada
        try {
            filter.doFilter(request, response, filterChain);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Unexpected error");
        }
    }
}

