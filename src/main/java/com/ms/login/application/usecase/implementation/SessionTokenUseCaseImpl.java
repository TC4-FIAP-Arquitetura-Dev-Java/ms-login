package com.ms.login.application.usecase.implementation;

import com.ms.login.application.usecase.SessionTokenUseCase;
import com.ms.login.domain.model.TokenInfoDomain;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionTokenUseCaseImpl implements SessionTokenUseCase {

    @Value("${jwt.refresh-token-secret}")
    private String REFRESH_TOKEN_SECRET;

    private final long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 7; // 7 dias

    private final Map<String, TokenInfoDomain> refreshTokens = new ConcurrentHashMap<>();

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(REFRESH_TOKEN_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    // Gera JWT refresh token e guarda em memória
    public String generateRefreshToken(String userId, String username) {
        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(REFRESH_TOKEN_EXPIRATION / 1000);

        String token = Jwts.builder()
                .setSubject(username)
                .claim("token_type", "refresh")
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        refreshTokens.put(token, new TokenInfoDomain(userId, username, expiresAt));
        return token;
    }

    @Override
    public TokenInfoDomain validateRefreshToken(String token) {
        try {
            // Valida assinatura e claims
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
        } catch (Exception e) {
            throw new SecurityException("Invalid refresh token");
        }

        TokenInfoDomain info = refreshTokens.get(token);
        if (info == null) throw new SecurityException("Refresh token not found");
        if (info.isRevoked()) throw new SecurityException("Refresh token revoked");
        if (info.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokens.remove(token);
            throw new SecurityException("Refresh token expired");
        }

        return info;
    }

    @Override
    public void revokeRefreshToken(String token) {
        TokenInfoDomain info = refreshTokens.get(token);
        if (info != null){
            info.setRevoked(true);
        }
    }

    @Override
    public void revokeAllUserTokens(Long userId) {
        refreshTokens.entrySet().removeIf(
                e -> e.getValue().getUserId().equals(userId));
    }

    @Override
    public void cleanupExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        refreshTokens.entrySet().removeIf(e -> e.getValue().getExpiresAt().isBefore(now));
    }
}
