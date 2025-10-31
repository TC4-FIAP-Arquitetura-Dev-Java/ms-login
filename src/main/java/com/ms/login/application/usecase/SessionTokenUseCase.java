package com.ms.login.application.usecase;

import com.ms.login.domain.model.TokenInfoDomain;

public interface SessionTokenUseCase {

    String generateRefreshToken(String userId, String username);

    TokenInfoDomain validateRefreshToken(String token);

    void revokeRefreshToken(String token);

    void revokeAllUserTokens(Long userId);

    void cleanupExpiredTokens();
}
