package io.bbahaida.bshort.service;

import io.bbahaida.bshort.model.User;
import io.bbahaida.bshort.repository.UserRepository;
import io.bbahaida.bshort.security.JwtProperties;
import io.bbahaida.bshort.security.Token;
import io.bbahaida.bshort.security.TokenUser;
import io.bbahaida.bshort.security.UserPass;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static io.bbahaida.bshort.model.enums.Role.USER;

@Service
public class UserService {


    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public User register(User user) {

        if (userExists(user.getUsername())) {
            throw new IllegalArgumentException(String.format("Ce username %s existe déja", user.getUsername()));
        }
        if (emailExists(user.getEmail())) {
            throw new IllegalArgumentException(String.format("Email %s already exists", user.getEmail()));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserActive(true);
        user.setRole(USER);
        return repository.save(user);
    }

    @SneakyThrows
    public Token login(UserPass userPass) {

        User user = repository.findByUsername(userPass.username).orElseThrow(IllegalAccessException::new);
        TokenUser tokenUser = user.toTokenUser();

        return Token.builder()
                .authenticated(true)
                .authToken(JwtProperties.generateToken(tokenUser, false, true))
                .refreshToken(JwtProperties.generateToken(tokenUser, true, true))
                //.user(user.toTokenUser())
                .build();
    }

    private boolean emailExists(String email) {
        return repository.existsByEmail(email);
    }

    private boolean userExists(String username) {
        return repository.existsByUsername(username);
    }
}
