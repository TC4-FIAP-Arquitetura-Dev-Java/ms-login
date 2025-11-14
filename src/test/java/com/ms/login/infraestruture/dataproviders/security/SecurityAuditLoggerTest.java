package com.ms.login.infraestruture.dataproviders.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.login.infrastructure.security.SecurityAuditLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityAuditLoggerTest {

    private SecurityAuditLogger auditLogger;
    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        // Configura o mock para retornar JSON válido
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"event\":\"TEST\"}");
        auditLogger = new SecurityAuditLogger(objectMapper);
    }

    @Test
    void logLoginAttempt_shouldCallObjectMapper() throws JsonProcessingException {
        auditLogger.logLoginAttempt("testuser", "192.168.1.1", true, "Success");

        verify(objectMapper, atLeastOnce()).writeValueAsString(any());
    }

    @Test
    void logLoginAttempt_shouldLogFailure() throws JsonProcessingException {
        auditLogger.logLoginAttempt("testuser", "192.168.1.1", false, "Invalid credentials");

        verify(objectMapper, atLeastOnce()).writeValueAsString(any());
    }

    @Test
    void logPasswordChange_shouldLogEvent() throws JsonProcessingException {
        auditLogger.logPasswordChange("testuser", "192.168.1.1");

        verify(objectMapper, atLeastOnce()).writeValueAsString(any());
    }

    @Test
    void logUserCreation_shouldLogEvent() throws JsonProcessingException {
        auditLogger.logUserCreation("newuser", "admin", "192.168.1.1");

        verify(objectMapper, atLeastOnce()).writeValueAsString(any());
    }

    @Test
    void logUserDeletion_shouldLogEvent() throws JsonProcessingException {
        auditLogger.logUserDeletion("deleteduser", "admin", "192.168.1.1");

        verify(objectMapper, atLeastOnce()).writeValueAsString(any());
    }

    @Test
    void logAccessDenied_shouldLogEvent() throws JsonProcessingException {
        auditLogger.logAccessDenied("testuser", "/api/admin", "192.168.1.1", "Insufficient privileges");

        verify(objectMapper, atLeastOnce()).writeValueAsString(any());
    }

    @Test
    void logTokenRefresh_shouldLogSuccess() throws JsonProcessingException {
        auditLogger.logTokenRefresh("testuser", "192.168.1.1", true);

        verify(objectMapper, atLeastOnce()).writeValueAsString(any());
    }

    @Test
    void logTokenRefresh_shouldLogFailure() throws JsonProcessingException {
        auditLogger.logTokenRefresh("testuser", "192.168.1.1", false);

        verify(objectMapper, atLeastOnce()).writeValueAsString(any());
    }

    @Test
    void logSuspiciousActivity_shouldLogEvent() throws JsonProcessingException {
        auditLogger.logSuspiciousActivity("testuser", "192.168.1.1", "Multiple failed logins", "5 attempts in 1 minute");

        verify(objectMapper, atLeastOnce()).writeValueAsString(any());
    }

    @Test
    void logLoginAttempt_shouldHandleException_whenObjectMapperFails() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("Error") {});

        // Não deve lançar exceção, deve tratar o erro internamente
        auditLogger.logLoginAttempt("testuser", "192.168.1.1", true, "Success");

        verify(objectMapper, atLeastOnce()).writeValueAsString(any());
    }
}

