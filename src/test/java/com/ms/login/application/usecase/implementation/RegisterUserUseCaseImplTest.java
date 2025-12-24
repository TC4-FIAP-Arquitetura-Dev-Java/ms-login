package com.ms.login.application.usecase.implementation;

import com.ms.login.application.gateway.LoginGateway;
import com.ms.login.application.gateway.SecretKeyGenerator;
import com.ms.login.domain.domainService.LoginDomainService;
import com.ms.login.domain.exceptions.CredentialLoginAlreadyExistsException;
import com.ms.login.domain.model.LoginDomain;
import com.ms.login.mocks.LoginMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseImplTest {

    @InjectMocks
    private RegisterUserUseCaseImpl useCase;

    @Mock
    private LoginGateway loginGateway;

    @Mock
    private LoginDomainService loginDomainService;

    @Mock
    private SecretKeyGenerator secretKeyGenerator;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldRegisterNewLoginSuccessfully() {
        // given
        var authentication = new TestingAuthenticationToken("user", "jwt-token");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LoginDomain domain = LoginMock.getLoginDomain();
        String originalPassword = domain.getPassword();
        String originalUsername = domain.getUsername();

        // when
        doNothing().when(loginDomainService).checkExistsUsername(domain.getUsername());
        doNothing().when(loginGateway).register(any(LoginDomain.class));
        when(secretKeyGenerator.encode(anyString())).thenReturn("encodedPassword");

//        useCase.register(domain);

        // then
        verify(loginDomainService).checkExistsUsername(originalUsername);
        verify(secretKeyGenerator).encode(originalPassword);
        verify(loginGateway).register(any(LoginDomain.class));
        assertThat(domain.getUsername()).isEqualTo(originalUsername.toLowerCase());
        assertThat(domain.getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        // given
        var authentication = new TestingAuthenticationToken("user", "jwt-token");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LoginDomain domain = LoginMock.getLoginDomain();

        // when
        doThrow(new CredentialLoginAlreadyExistsException("Usuário já cadastrado"))
                .when(loginDomainService)
                .checkExistsUsername(domain.getUsername());

        // then
//        assertThrows(CredentialLoginAlreadyExistsException.class, () -> useCase.register(domain));
        verify(loginDomainService).checkExistsUsername(domain.getUsername());
        verifyNoInteractions(loginGateway);
        verifyNoInteractions(secretKeyGenerator);
    }

    @Test
    void shouldEncodePasswordAndLowercaseUsername() {
        // given
        var authentication = new TestingAuthenticationToken("user", "jwt-token");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LoginDomain domain = LoginMock.getLoginDomain();
        domain.setUsername("TestUser");
        domain.setPassword("plainPassword");

        // when
        doNothing().when(loginDomainService).checkExistsUsername(anyString());
        doNothing().when(loginGateway).register(any(LoginDomain.class));
        when(secretKeyGenerator.encode("plainPassword")).thenReturn("encodedPassword");

//        useCase.register(domain);

        // then
        assertThat(domain.getUsername()).isEqualTo("testuser");
        assertThat(domain.getPassword()).isEqualTo("encodedPassword");
        verify(secretKeyGenerator).encode("plainPassword");
    }
}
