package com.ms.login.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.security.core.AuthenticationException;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final MyUserDetailsService myUserDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider,
                                   MyUserDetailsService myUserDetailsService) {
        this.tokenProvider = tokenProvider;
        this.myUserDetailsService = myUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String header = request.getHeader("Authorization");

            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);

                if (tokenProvider.validateToken(token)) {
                    String username = tokenProvider.getUsernameFromToken(token);
                    UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

            filterChain.doFilter(request, response);

        } catch (AuthenticationException ex) {
            // Delega o tratamento para o AuthenticationEntryPoint configurado
            SecurityContextHolder.clearContext();
            getFailureHandler(request, response).commence(request, response, ex);

        } catch (Exception ex) {
            // Qualquer outra exceção genérica vai para o seu GlobalExceptionHandler
            throw ex;
        }
    }

    private AuthenticationEntryPoint getFailureHandler(HttpServletRequest request, HttpServletResponse response) {
        return new CustomAuthenticationEntryPoint();
    }
}
