package com.ms.login.entrypoint.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
//@RequestMapping("/v1/auth")
public class AuthController {

    @GetMapping("/public")
    public String publicAuth() {
        return "<h3>Rota Publica, aqui voce tem acesso </h3>";
    }

    @GetMapping("/private")
    public String privateAuth(@AuthenticationPrincipal Principal principal) {
        return String.format("<h3>Rota Privada, somente os com acesso entra aqui %s </h3>", principal);
    }


}
