package com.ms.login.infrastructure.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Pattern;

//Filtro de validação para evitar tipos de ataques ao serviço de login, como por exemplo SQL_INJECTION

@Component
public class InputValidationFilter extends OncePerRequestFilter {

    // Padrões para validação
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    private static final Pattern USERNAME_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._-]{3,20}$");

    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile(
            "(?i).*('|(\\-\\-)|(;)|(\\|)|(\\*)|(%)|(\\+)|(\\=)|(\\<)|(\\>)|(\\[)|(\\])|(\\{)|(\\})|(\\()|(\\))|(union)|(select)|(insert)|(update)|(delete)|(drop)|(create)|(alter)|(exec)|(execute)).*");

    private static final Pattern XSS_PATTERN = Pattern.compile(
            "(?i).*(<script|</script|javascript:|on\\w+\\s*=|<iframe|</iframe|<object|</object|<embed|</embed|<link|</link|<meta|</meta|<style|</style).*");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Validar parâmetros da URL
        if (hasMaliciousParameters(request)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Invalid input detected\"}");
            return;
        }

        // Validar headers suspeitos
        if (hasSuspiciousHeaders(request)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Suspicious request detected\"}");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean hasMaliciousParameters(HttpServletRequest request) {
        // Verificar parâmetros da query string
        String queryString = request.getQueryString();
        if (queryString != null) {
            if (SQL_INJECTION_PATTERN.matcher(queryString).matches() ||
                    XSS_PATTERN.matcher(queryString).matches()) {
                return true;
            }
        }

        // Verificar parâmetros individuais
        return request.getParameterMap().values().stream()
                .flatMap(java.util.Arrays::stream)
                .anyMatch(value ->
                        SQL_INJECTION_PATTERN.matcher(value).matches() ||
                                XSS_PATTERN.matcher(value).matches());
    }

    private boolean hasSuspiciousHeaders(HttpServletRequest request) {
        // Verificar User-Agent suspeito
        String userAgent = request.getHeader("User-Agent");
        if (userAgent != null && (
                userAgent.length() > 500 || // User-Agent muito longo
                        XSS_PATTERN.matcher(userAgent).matches())) {
            return true;
        }

        // Verificar outros headers suspeitos
        String[] suspiciousHeaders = {
                "X-Forwarded-For", "X-Real-IP", "X-Originating-IP"
        };

        for (String header : suspiciousHeaders) {
            String value = request.getHeader(header);
            if (value != null && (
                    value.length() > 100 || // Header muito longo
                            SQL_INJECTION_PATTERN.matcher(value).matches())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }

    public static boolean containsMaliciousContent(String input) {
        if (input == null) return false;
        return SQL_INJECTION_PATTERN.matcher(input).matches() ||
                XSS_PATTERN.matcher(input).matches();
    }
}
