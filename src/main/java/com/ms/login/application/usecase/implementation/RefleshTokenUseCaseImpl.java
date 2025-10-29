package com.ms.login.application.usecase.implementation;

import com.ms.login.application.gateway.LoginGateway;
import com.ms.login.application.usecase.RefreshTokenUseCase;
import com.ms.login.domain.enums.RoleEnum;
import com.ms.login.domain.model.AuthTokenDomain;
import com.ms.login.infrastructure.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
            if(!jwtTokenProvider.validateRefreshToken(refreshToken)) {
                throw new BadCredentialsException("Invalid refresh token");
            }

            Claims claims = jwtTokenProvider.extractClaimsRefreshToken(refreshToken);
            String username = jwtTokenProvider.extractUsername(claims);
            String userId = jwtTokenProvider.extractUserId(claims);
            RoleEnum role = jwtTokenProvider.extractRole(claims);

            loginGateway.getUsername(username).
                    orElseThrow(() -> new UsernameNotFoundException("Credential user not found"));

            String newAcessToken = jwtTokenProvider.generateAccessToken(username, userId, role);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(refreshToken);

            Claims newClaims = jwtTokenProvider.extractClaimsRefreshToken(newRefreshToken);
            Date expiresAt = jwtTokenProvider.extractExpirationDate(newClaims);
            return new AuthTokenDomain(newAcessToken, newRefreshToken, username, expiresAt.toString(), userId);
        }catch (ExpiredJwtException e){
            throw new BadCredentialsException("Expired refresh token");
        }catch (Exception e) {
            throw new BadCredentialsException("Invalid refresh token");
        }
    }
}
