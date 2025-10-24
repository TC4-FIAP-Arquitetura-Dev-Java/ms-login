package com.ms.login.application.usecase.implementation;

import com.ms.login.application.gateway.LoginGateway;
import com.ms.login.application.usecase.RefreshTokenUseCase;
import com.ms.login.domain.model.AuthTokenDomain;
import com.ms.login.infrastructure.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Date;

public class RefleshTokenUseCaseImpl implements RefreshTokenUseCase {

    private final JwtTokenProvider jwtTokenProvider;
    private final LoginGateway loginGateway;

    public RefleshTokenUseCaseImpl(JwtTokenProvider jwtTokenProvider, LoginGateway loginGateway) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.loginGateway = loginGateway;
    }

    @Override
    public AuthTokenDomain refreshToken(String refreshToken) {
        try{
            //Valida o refreshtoken
            if(!jwtTokenProvider.validateToken(refreshToken)) {
                throw new BadCredentialsException("Invalid refresh token");
            }

            Claims claims = jwtTokenProvider.extractClaims(refreshToken);
            String username = jwtTokenProvider.extractUsername(claims);
            String userId = jwtTokenProvider.extractUserId(claims);

            //TODO: HABILITAR APENAS EM QUANDO O GATEWAY TIVER IMPLEMENTADO COM ESSA FUNÇÃO
//            loginGateway.getUsername(username).
//                    orElseThrow(() -> new UsernameNotFoundException("Credential user not found"));

            String newAcessToken = jwtTokenProvider.generateAccessToken(username);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(refreshToken);

            Claims newClaims = jwtTokenProvider.extractClaims(newRefreshToken);
            Date expiresAt = jwtTokenProvider.extractExpirationDate(newClaims);
            return new AuthTokenDomain(newAcessToken, newRefreshToken, username, expiresAt.toString(), userId);
        }catch (ExpiredJwtException e){
            throw new BadCredentialsException("Expired refresh token");
        }catch (Exception e) {
            throw new BadCredentialsException("Invalid refresh token");
        }
    }
}
