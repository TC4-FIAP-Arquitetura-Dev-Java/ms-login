package com.ms.login.application.usecase.implementation;

import com.ms.login.application.gateway.LoginGateway;
import com.ms.login.application.usecase.RefreshTokenUseCase;
import com.ms.login.application.usecase.SessionTokenUseCase;
import com.ms.login.domain.model.AuthTokenDomain;
import com.ms.login.domain.model.TokenInfoDomain;
import com.ms.login.infrastructure.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Date;

public class RefleshUseCaseImpl implements RefreshTokenUseCase {

    private final JwtTokenProvider jwtTokenProvider;
    private final LoginGateway loginGateway;
    private final SessionTokenUseCase sessionTokenUseCase;

    public RefleshUseCaseImpl(JwtTokenProvider jwtTokenProvider, LoginGateway loginGateway, SessionTokenUseCase sessionTokenUseCase) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.loginGateway = loginGateway;
        this.sessionTokenUseCase = sessionTokenUseCase;
    }

    @Override
    public AuthTokenDomain refreshToken(String refreshToken) {
        try{
            // Valida o refresh token armazenado
            TokenInfoDomain info = sessionTokenUseCase.validateRefreshToken(refreshToken);

            // Busca o usuário
            var user = loginGateway.getUsername(info.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Rotaciona refresh token
            sessionTokenUseCase.revokeRefreshToken(refreshToken);
            String newRefreshToken = sessionTokenUseCase.generateRefreshToken(user);

            // Gera novo access token
            String newAccessToken = jwtTokenProvider.generateAccessToken(
                    user.getUsername(), user.getUserId(), user.getRoleEnum());

            Claims claims = jwtTokenProvider.extractClaimsAcessToken(newAccessToken);
            Date expiresAt = jwtTokenProvider.extractExpirationDate(claims);

            return new AuthTokenDomain(
                    newAccessToken,
                    newRefreshToken,
                    user.getUsername(),
                    user.getRoleEnum().name(),
                    user.getUserId(),
                    expiresAt.toString()
            );
        }catch (ExpiredJwtException e){
            throw new BadCredentialsException("Expired refresh token");
        }catch (Exception e) {
            throw new BadCredentialsException("Invalid refresh token");
        }
    }
}
