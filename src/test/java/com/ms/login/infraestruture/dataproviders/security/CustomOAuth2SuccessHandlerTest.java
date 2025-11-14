package com.ms.login.infraestruture.dataproviders.security;

import com.ms.login.infrastructure.security.CustomOAuth2SuccessHandler;
import com.ms.login.infrastructure.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomOAuth2SuccessHandlerTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private CustomOAuth2SuccessHandler successHandler;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void onAuthenticationSuccess_shouldGenerateToken() throws IOException {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testuser");
        when(jwtTokenProvider.generateAccessToken(anyString(), any(), any())).thenReturn("test-token");

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(jwtTokenProvider).generateAccessToken("testuser", null, null);
    }

    @Test
    void onAuthenticationSuccess_shouldSetJsonContentType() throws IOException {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testuser");
        when(jwtTokenProvider.generateAccessToken(anyString(), any(), any())).thenReturn("test-token");

        successHandler.onAuthenticationSuccess(request, response, authentication);

        assertThat(response.getContentType()).isEqualTo("application/json");
    }

    @Test
    void onAuthenticationSuccess_shouldWriteTokenInResponse() throws IOException {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testuser");
        when(jwtTokenProvider.generateAccessToken(anyString(), any(), any())).thenReturn("jwt-token-123");

        successHandler.onAuthenticationSuccess(request, response, authentication);

        String content = response.getContentAsString();
        assertThat(content).contains("token");
        assertThat(content).contains("jwt-token-123");
    }
}

