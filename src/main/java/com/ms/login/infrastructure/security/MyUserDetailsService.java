package com.ms.login.infrastructure.security;

import com.ms.login.application.port.out.UserGateway;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserGateway userGateway;

    public MyUserDetailsService(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            return userGateway.getUserByUsername(username)
                    .map(MyUserDetails::new)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        }catch (Exception e){
            throw new UsernameNotFoundException("Failed to find user credentials. " + e.getMessage());
        }
    }
}
