package com.ms.login.domain.domainService.implementation;

import com.ms.login.application.gateway.LoginGateway;
import com.ms.login.domain.exceptions.CredentialLoginAlreadyExistsException;
import com.ms.login.domain.model.LoginDomain;
import com.ms.login.mocks.LoginMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginDomainServiceImplTest {

    @Mock
    private LoginGateway loginGateway;

    @InjectMocks
    private LoginDomainServiceImpl loginDomainService;

    @Test
    void shouldNotThrowExceptionWhenUsernameDoesNotExist() {
        String username = "newuser";

        when(loginGateway.getUsername(username)).thenReturn(Optional.empty());
        loginDomainService.checkExistsUsername(username);

        // then
        verify(loginGateway).getUsername(username);
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        // given
        String username = "existinguser";
        LoginDomain existingLogin = LoginMock.getLoginDomain();

        // when
        when(loginGateway.getUsername(username)).thenReturn(Optional.of(existingLogin));

        // then
        assertThrows(CredentialLoginAlreadyExistsException.class,
                () -> loginDomainService.checkExistsUsername(username));

        verify(loginGateway).getUsername(username);
    }
}

