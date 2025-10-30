package com.ms.login.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final Map<String, AttemptInfo> attempts = new ConcurrentHashMap<>();
    private static final int MAX_ATTEMPTS = 5;
    private static final int LOCKOUT_DURATION_MINUTES = 15;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        if (isLoginEndpoint(request)) {
            String clientIp = getClientIpAddress(request);
            AttemptInfo attemptInfo = attempts.get(clientIp);
            
            if (attemptInfo != null && attemptInfo.isLocked()) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Too many login attempts. Please try again later.\"}");
                return;
            }
        }
        
        filterChain.doFilter(request, response);
    }

    public void recordFailedAttempt(String clientIp) {
        AttemptInfo attemptInfo = attempts.computeIfAbsent(clientIp, k -> new AttemptInfo());
        attemptInfo.incrementAttempts();
        
        if (attemptInfo.getAttempts() >= MAX_ATTEMPTS) {
            attemptInfo.lockUntil(LocalDateTime.now().plusMinutes(LOCKOUT_DURATION_MINUTES));
        }
    }

    public void clearAttempts(String clientIp) {
        attempts.remove(clientIp);
    }

    private boolean isLoginEndpoint(HttpServletRequest request) {
        return "POST".equals(request.getMethod()) && 
               request.getRequestURI().contains("/login");
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private static class AttemptInfo {
        private int attempts = 0;
        private LocalDateTime lockedUntil;

        public void incrementAttempts() {
            this.attempts++;
        }

        public int getAttempts() {
            return attempts;
        }

        public void lockUntil(LocalDateTime lockedUntil) {
            this.lockedUntil = lockedUntil;
        }

        public boolean isLocked() {
            return lockedUntil != null && LocalDateTime.now().isBefore(lockedUntil);
        }
    }
}

