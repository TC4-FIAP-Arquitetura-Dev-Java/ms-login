package com.ms.login.infrastructure.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

//Logger de auditoria de segurança

@Component
public class SecurityAuditLogger {

    private static final Logger auditLogger = LoggerFactory.getLogger("SECURITY_AUDIT");
    private final ObjectMapper objectMapper;

    public SecurityAuditLogger(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void logLoginAttempt(String username, String clientIp, boolean success, String reason) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "LOGIN_ATTEMPT");
        logData.put("timestamp", LocalDateTime.now());
        logData.put("username", username);
        logData.put("clientIp", clientIp);
        logData.put("success", success);
        logData.put("reason", reason);

        try {
            auditLogger.info("SECURITY_EVENT: {}", objectMapper.writeValueAsString(logData));
        } catch (Exception e) {
            auditLogger.error("Failed to log security event", e);
        }
    }

    public void logPasswordChange(String username, String clientIp) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "PASSWORD_CHANGE");
        logData.put("timestamp", LocalDateTime.now());
        logData.put("username", username);
        logData.put("clientIp", clientIp);

        try {
            auditLogger.info("SECURITY_EVENT: {}", objectMapper.writeValueAsString(logData));
        } catch (Exception e) {
            auditLogger.error("Failed to log security event", e);
        }
    }

    public void logUserCreation(String username, String createdBy, String clientIp) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "USER_CREATION");
        logData.put("timestamp", LocalDateTime.now());
        logData.put("username", username);
        logData.put("createdBy", createdBy);
        logData.put("clientIp", clientIp);

        try {
            auditLogger.info("SECURITY_EVENT: {}", objectMapper.writeValueAsString(logData));
        } catch (Exception e) {
            auditLogger.error("Failed to log security event", e);
        }
    }

    public void logUserDeletion(String username, String deletedBy, String clientIp) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "USER_DELETION");
        logData.put("timestamp", LocalDateTime.now());
        logData.put("username", username);
        logData.put("deletedBy", deletedBy);
        logData.put("clientIp", clientIp);

        try {
            auditLogger.info("SECURITY_EVENT: {}", objectMapper.writeValueAsString(logData));
        } catch (Exception e) {
            auditLogger.error("Failed to log security event", e);
        }
    }

    public void logAccessDenied(String username, String resource, String clientIp, String reason) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "ACCESS_DENIED");
        logData.put("timestamp", LocalDateTime.now());
        logData.put("username", username);
        logData.put("resource", resource);
        logData.put("clientIp", clientIp);
        logData.put("reason", reason);

        try {
            auditLogger.warn("SECURITY_EVENT: {}", objectMapper.writeValueAsString(logData));
        } catch (Exception e) {
            auditLogger.error("Failed to log security event", e);
        }
    }

    public void logTokenRefresh(String username, String clientIp, boolean success) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "TOKEN_REFRESH");
        logData.put("timestamp", LocalDateTime.now());
        logData.put("username", username);
        logData.put("clientIp", clientIp);
        logData.put("success", success);

        try {
            auditLogger.info("SECURITY_EVENT: {}", objectMapper.writeValueAsString(logData));
        } catch (Exception e) {
            auditLogger.error("Failed to log security event", e);
        }
    }

    public void logSuspiciousActivity(String username, String clientIp, String activity, String details) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "SUSPICIOUS_ACTIVITY");
        logData.put("timestamp", LocalDateTime.now());
        logData.put("username", username);
        logData.put("clientIp", clientIp);
        logData.put("activity", activity);
        logData.put("details", details);

        try {
            auditLogger.error("SECURITY_EVENT: {}", objectMapper.writeValueAsString(logData));
        } catch (Exception e) {
            auditLogger.error("Failed to log security event", e);
        }
    }
}
