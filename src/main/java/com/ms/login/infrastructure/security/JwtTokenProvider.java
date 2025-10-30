package com.ms.login.infrastructure.security;

import com.ms.login.domain.enums.RoleEnum;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.access-token-secret}")
    private String ACCESS_TOKEN_SECRET;

    @Value("${jwt.refresh-token-secret}")
    private String REFRESH_TOKEN_SECRET;

    private final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60; // 1 hora
    private final long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 7; // 7 dias

    //Access Token
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    //Refresh Token
    private Key getSigningKeyFromRefreshToken() {
        return Keys.hmacShaKeyFor(REFRESH_TOKEN_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String username, String userId, RoleEnum roleEnum) {
        return Jwts.builder()
                .setSubject(username)
                .claim("token_type", "access")
                .claim("userId", userId)
                .claim("role", roleEnum)
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("token_type", "refresh")
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(getSigningKeyFromRefreshToken(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaimsAcessToken(String token){
        return extractClaims(token, getSigningKey());
    }

    public Claims extractClaimsRefreshToken(String token){
        return extractClaims(token, getSigningKeyFromRefreshToken());
    }

    private Claims extractClaims(String token, Key typeToken) {
        return Jwts.parserBuilder()
                .setSigningKey(typeToken)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isValidAcessToken(String token) {
        return isValid(token, ACCESS_TOKEN_SECRET, "access");
    }

    public boolean isValidRefleshToken(String token){
        return isValid(token, REFRESH_TOKEN_SECRET, "refresh");
    }

    private boolean isValid(String token, String secret, String expectedType){
        try{
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return expectedType.equals(claims.get("token_type"));
        }catch (JwtException e){
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean validateToken(String accessToken) {
        try{
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(accessToken);
            return true;
        }catch (JwtException | IllegalArgumentException e){
            return false;
        }
    }

    public RoleEnum extractRole(Claims claims) {
        String roleStr = claims.get("role", String.class);
        return RoleEnum.valueOf(roleStr);
    }

    public String extractUsername(Claims claims) {
        return claims.getSubject();
    }

    public String extractUserId(Claims claims) {
        return claims.get("userId", String.class);
    }

    public String extractEmail(Claims claims) {
        return claims.get("email", String.class);
    }

    public Date extractExpirationDate(Claims claims) {
        return claims.getExpiration();
    }

    public Date extractExpirationDateFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}
