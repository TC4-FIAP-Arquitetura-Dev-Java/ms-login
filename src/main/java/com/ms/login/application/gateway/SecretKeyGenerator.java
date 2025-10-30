package com.ms.login.application.gateway;

public interface SecretKeyGenerator {

    String encode(CharSequence rawPassword);
}
