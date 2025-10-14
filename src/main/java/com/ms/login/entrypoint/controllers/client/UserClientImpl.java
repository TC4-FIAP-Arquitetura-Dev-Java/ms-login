package com.ms.login.entrypoint.controllers.client;

public class UserClientImpl {

    private final UserFeightClient userFeightClient;

    public UserClientImpl(UserFeightClient userFeightClient) {
        this.userFeightClient = userFeightClient;
    }
}
