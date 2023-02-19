package io.bbahaida.bshort.web;

import io.bbahaida.bshort.model.User;
import io.bbahaida.bshort.security.Token;
import io.bbahaida.bshort.security.TokenUser;
import io.bbahaida.bshort.security.UserPass;
import io.bbahaida.bshort.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/auth")
public class AuthController {

    @Autowired
    private UserService service;


    @PostMapping("/register")
    public TokenUser register(@RequestBody User user) {
        return service.register(user).toTokenUser();
    }
    @PostMapping("/login")
    public Token login(@RequestBody UserPass user) {
        return service.login(user);
    }
}
