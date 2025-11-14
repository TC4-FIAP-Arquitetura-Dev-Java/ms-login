package com.ms.login.infraestruture.dataproviders.security;

import com.ms.login.infrastructure.security.ClientIpResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ClientIpResolverTest {

    private ClientIpResolver clientIpResolver;

    @BeforeEach
    void setUp() {
        clientIpResolver = new ClientIpResolver();
    }

    @Test
    void resolve_shouldReturnXForwardedFor_whenHeaderIsPresent() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Forwarded-For", "192.168.1.1, 10.0.0.1");
        request.setRemoteAddr("127.0.0.1");
        
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);

        String result = clientIpResolver.resolve();

        assertThat(result).isEqualTo("192.168.1.1");
        
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void resolve_shouldReturnRemoteAddr_whenXForwardedForIsNotPresent() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("192.168.1.100");
        
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);

        String result = clientIpResolver.resolve();

        assertThat(result).isEqualTo("192.168.1.100");
        
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void resolve_shouldReturnFirstIp_whenXForwardedForHasMultipleIps() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Forwarded-For", "203.0.113.1, 198.51.100.1, 192.0.2.1");
        request.setRemoteAddr("127.0.0.1");
        
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);

        String result = clientIpResolver.resolve();

        assertThat(result).isEqualTo("203.0.113.1");
        
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void resolve_shouldReturnUNKNOWN_whenRequestAttributesAreNull() {
        RequestContextHolder.resetRequestAttributes();

        String result = clientIpResolver.resolve();

        assertThat(result).isEqualTo("UNKNOWN");
    }

    @Test
    void resolve_shouldTrimWhitespace_whenXForwardedForHasSpaces() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Forwarded-For", "  192.168.1.1  , 10.0.0.1  ");
        request.setRemoteAddr("127.0.0.1");
        
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);

        String result = clientIpResolver.resolve();

        assertThat(result).isEqualTo("192.168.1.1");
        
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void resolve_shouldReturnRemoteAddr_whenXForwardedForIsEmpty() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        // Não adicionar o header X-Forwarded-For (null)
        request.setRemoteAddr("192.168.1.200");
        
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);

        String result = clientIpResolver.resolve();

        assertThat(result).isEqualTo("192.168.1.200");
        
        RequestContextHolder.resetRequestAttributes();
    }
}

