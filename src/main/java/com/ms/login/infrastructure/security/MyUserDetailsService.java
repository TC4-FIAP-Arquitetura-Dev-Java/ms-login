package com.ms.login.infrastructure.security;

import com.ms.login.infrastructure.database.entities.LoginDocument;
import com.ms.login.infrastructure.database.repositories.LoginRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final LoginRepository loginRepository;

    public MyUserDetailsService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            LoginDocument login = loginRepository.findByUsername(username);

            if(login == null){
                throw new UsernameNotFoundException("Usuário não encontrado");
            }

            return new MyUserDetails(login);
        }catch (Exception e){
            throw new UsernameNotFoundException("Erro ao buscar credenciais " + e.getMessage());
        }
    }
}
