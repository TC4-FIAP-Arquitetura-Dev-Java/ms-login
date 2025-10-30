package com.ms.login.infrastructure.security.password;

import com.ms.login.application.gateway.SecretKeyGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecretKeyGeneratorImpl implements SecretKeyGenerator {

    private final PasswordEncoder passwordEncoder;

    public SecretKeyGeneratorImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(CharSequence password) {
        return passwordEncoder.encode(password);
    }
}
