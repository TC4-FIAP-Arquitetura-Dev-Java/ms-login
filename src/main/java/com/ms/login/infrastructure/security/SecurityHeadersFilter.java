package com.ms.login.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityHeadersFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // Prevenir clickjacking
        response.setHeader("X-Frame-Options", "DENY");
        
        // Prevenir MIME type sniffing
        response.setHeader("X-Content-Type-Options", "nosniff");
        
        // Habilitar XSS protection
        response.setHeader("X-XSS-Protection", "1; mode=block");
        
        // Política de referrer
        response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        
        // Content Security Policy
        response.setHeader("Content-Security-Policy", 
            "default-src 'self'; " +
            "script-src 'self' 'unsafe-inline' 'unsafe-eval'; " +
            "style-src 'self' 'unsafe-inline'; " +
            "img-src 'self' data: https:; " +
            "font-src 'self'; " +
            "connect-src 'self'; " +
            "frame-ancestors 'none';");
        
        // Permissions Policy (antigo Feature Policy)
        response.setHeader("Permissions-Policy", 
            "geolocation=(), " +
            "microphone=(), " +
            "camera=(), " +
            "payment=(), " +
            "usb=(), " +
            "magnetometer=(), " +
            "gyroscope=(), " +
            "speaker=(), " +
            "vibrate=(), " +
            "fullscreen=(self), " +
            "sync-xhr=()");
        
        // Strict Transport Security (apenas em HTTPS)
        if (request.isSecure()) {
            response.setHeader("Strict-Transport-Security", 
                "max-age=31536000; includeSubDomains; preload");
        }
        
        // Cache Control para endpoints sensíveis
        if (isSensitiveEndpoint(request)) {
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, private");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
        }
        
        filterChain.doFilter(request, response);
    }

    private boolean isSensitiveEndpoint(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.contains("/login") || 
               uri.contains("/users") || 
               uri.contains("/auth") ||
               uri.contains("/admin");
    }
}

