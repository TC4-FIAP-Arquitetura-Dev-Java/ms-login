package com.ms.login.infraestruture.dataproviders.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.login.infrastructure.security.SecurityAuditLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityAuditLoggerTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private SecurityAuditLogger auditLogger;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(any()))
                .thenReturn("{\"event\":\"TEST\"}");
    }

    // ========== SUCESSO ==========

    @Test
    void shouldLogLoginAttempt() throws JsonProcessingException {
        auditLogger.logLoginAttempt("testuser", "192.168.1.1", true, "Success");
        verify(objectMapper).writeValueAsString(any());
    }

    @Test
    void shouldLogLoginFailure() throws JsonProcessingException {
        auditLogger.logLoginAttempt("testuser", "192.168.1.1", false, "Invalid credentials");
        verify(objectMapper).writeValueAsString(any());
    }

    @Test
    void shouldLogPasswordChange() throws JsonProcessingException {
        auditLogger.logPasswordChange("testuser", "192.168.1.1");
        verify(objectMapper).writeValueAsString(any());
    }

    @Test
    void shouldLogUserCreation() throws JsonProcessingException {
        auditLogger.logUserCreation("newuser", "admin", "192.168.1.1");
        verify(objectMapper).writeValueAsString(any());
    }

    @Test
    void shouldLogUserDeletion() throws JsonProcessingException {
        auditLogger.logUserDeletion("deleteduser", "admin", "192.168.1.1");
        verify(objectMapper).writeValueAsString(any());
    }

    @Test
    void shouldLogAccessDenied() throws JsonProcessingException {
        auditLogger.logAccessDenied("testuser", "/api/admin", "192.168.1.1", "Insufficient privileges");
        verify(objectMapper).writeValueAsString(any());
    }

    @Test
    void shouldLogTokenRefreshSuccess() throws JsonProcessingException {
        auditLogger.logTokenRefresh("testuser", "192.168.1.1", true);
        verify(objectMapper).writeValueAsString(any());
    }

    @Test
    void shouldLogTokenRefreshFailure() throws JsonProcessingException {
        auditLogger.logTokenRefresh("testuser", "192.168.1.1", false);
        verify(objectMapper).writeValueAsString(any());
    }

    @Test
    void shouldLogSuspiciousActivity() throws JsonProcessingException {
        auditLogger.logSuspiciousActivity("testuser", "192.168.1.1", "Multiple failed logins", "5 attempts in 1 minute");
        verify(objectMapper).writeValueAsString(any());
    }

    // ========== EXCEÇÕES (Map falhando) ==========
    private void mockObjectMapperFailure() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(any()))
                .thenThrow(new JsonProcessingException("Error") {});
    }

    @Test
    void shouldHandleExceptionOnLoginAttempt() throws Exception {
        mockObjectMapperFailure();
        auditLogger.logLoginAttempt("testuser", "192.168.1.1", true, "Success");
        verify(objectMapper).writeValueAsString(any());
    }

    @Test
    void shouldHandleExceptionOnPasswordChange() throws Exception {
        mockObjectMapperFailure();
        auditLogger.logPasswordChange("testuser", "192.168.1.1");
        verify(objectMapper).writeValueAsString(any());
    }

    @Test
    void shouldHandleExceptionOnUserCreation() throws Exception {
        mockObjectMapperFailure();
        auditLogger.logUserCreation("newuser", "admin", "192.168.1.1");
        verify(objectMapper).writeValueAsString(any());
    }

    @Test
    void shouldHandleExceptionOnUserDeletion() throws Exception {
        mockObjectMapperFailure();
        auditLogger.logUserDeletion("deleteduser", "admin", "192.168.1.1");
        verify(objectMapper).writeValueAsString(any());
    }

    @Test
    void shouldHandleExceptionOnAccessDenied() throws Exception {
        mockObjectMapperFailure();
        auditLogger.logAccessDenied("testuser", "/api/admin", "192.168.1.1", "Insufficient privileges");
        verify(objectMapper).writeValueAsString(any());
    }

    @Test
    void shouldHandleExceptionOnTokenRefresh() throws Exception {
        mockObjectMapperFailure();
        auditLogger.logTokenRefresh("testuser", "192.168.1.1", true);
        verify(objectMapper).writeValueAsString(any());
    }

    @Test
    void shouldHandleExceptionOnSuspiciousActivity() throws Exception {
        mockObjectMapperFailure();
        auditLogger.logSuspiciousActivity("testuser", "192.168.1.1", "Multiple failed logins", "5 attempts in 1 minute");
        verify(objectMapper).writeValueAsString(any());
    }
}
