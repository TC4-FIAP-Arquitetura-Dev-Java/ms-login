package com.ms.login.infraestruture.dataproviders.security;

import com.ms.login.infrastructure.security.CustomAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationEntryPointTest {

    @InjectMocks
    private CustomAuthenticationEntryPoint entryPoint;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private AuthenticationException authException;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        authException = new AuthenticationException("Authentication failed") {};
    }

    @Test
    void commence_shouldSetUnauthorizedStatus() throws IOException {
        request.setRequestURI("/api/protected");

        entryPoint.commence(request, response, authException);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void commence_shouldSetJsonContentType() throws IOException {
        request.setRequestURI("/api/protected");

        entryPoint.commence(request, response, authException);

        assertThat(response.getContentType()).isEqualTo("application/json");
    }

    @Test
    void commence_shouldWriteApiErrorJson() throws IOException {
        request.setRequestURI("/api/protected");

        entryPoint.commence(request, response, authException);

        String content = response.getContentAsString();
        assertThat(content).contains("\"status\":401");
        assertThat(content).contains("\"error\":\"Unauthorized\"");
        assertThat(content).contains("\"path\":\"/api/protected\"");
    }

    @Test
    void commence_shouldHandleException_whenJsonWritingFails() {
        request.setRequestURI("/api/protected");
        
        // Criar um response que falha ao escrever
        HttpServletResponse failingResponse = new MockHttpServletResponse() {
            @Override
            public java.io.PrintWriter getWriter() {
                throw new RuntimeException("Write failed");
            }
        };

        try {
            entryPoint.commence(request, failingResponse, authException);
        } catch (IOException e) {
            // Esperado em caso de falha
        }

        // Deve enviar erro 401 mesmo em caso de falha
        assertThat(failingResponse.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
    }
}

