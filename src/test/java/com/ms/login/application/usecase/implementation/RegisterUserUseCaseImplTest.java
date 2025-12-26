package com.ms.login.application.usecase.implementation;

import com.ms.login.application.gateway.LoginGateway;
import com.ms.login.application.gateway.SecretKeyGenerator;
import com.ms.login.application.port.out.UserGateway;
import com.ms.login.domain.domainService.LoginDomainService;
import com.ms.login.domain.exceptions.CredentialLoginAlreadyExistsException;
import com.ms.login.domain.model.LoginDomain;
import com.ms.login.domain.model.UserDomain;
import com.ms.login.mocks.LoginMock;
import com.ms.login.mocks.UserMock;
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
    private UserGateway userGateway;

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
        var authentication = new TestingAuthenticationToken("user", "jwt-token");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDomain domain = UserMock.getUserDomain();
        String originalPassword = domain.getPassword();
        String originalUsername = domain.getUsername();

        // mocks
        doNothing().when(loginDomainService).checkExistsUsername(originalUsername);
        when(secretKeyGenerator.encode(originalPassword)).thenReturn("encodedPassword");
        doNothing().when(userGateway).createUser(any(UserDomain.class));

        // *** CHAMADA QUE FALTAVA ***
        useCase.register(domain);

        // verificações
        verify(loginDomainService).checkExistsUsername(originalUsername);
        verify(secretKeyGenerator).encode(originalPassword);
        verify(userGateway).createUser(any(UserDomain.class));
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
        verifyNoInteractions(userGateway);
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
        doNothing().when(userGateway).createUser(any(UserDomain.class));
        when(secretKeyGenerator.encode("plainPassword")).thenReturn("encodedPassword");

//        useCase.register(domain);

        // then
        assertThat(domain.getUsername()).isEqualTo("testuser");
        assertThat(domain.getPassword()).isEqualTo("encodedPassword");
        verify(secretKeyGenerator).encode("plainPassword");
    }
}
