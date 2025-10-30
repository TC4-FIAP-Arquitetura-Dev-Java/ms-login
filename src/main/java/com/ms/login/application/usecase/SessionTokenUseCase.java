package com.ms.login.application.usecase;

import com.ms.login.domain.model.TokenInfoDomain;
import com.ms.login.infrastructure.security.MyUserDetails;

public interface SessionTokenUseCase {

    String generateRefreshToken(MyUserDetails user);

    TokenInfoDomain validateRefreshToken(String token);

    void revokeRefreshToken(String token);

    void revokeAllUserTokens(Long userId);

    void cleanupExpiredTokens();
}
