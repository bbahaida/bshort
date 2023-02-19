package io.bbahaida.bshort.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bbahaida.bshort.model.User;
import io.bbahaida.bshort.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private TokenUser tokenUser = null;


    JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    /* Trigger when we issue POST request to /login
      We also need to pass in {"username":"user", "password":"pass"} in the request body
    */
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // Grab credentials and map them to login viewmodel
        try {
            tokenUser = new ObjectMapper().readValue(request.getInputStream(), TokenUser.class);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }


        UsernamePasswordAuthenticationToken authenticationToken = null;
        if (tokenUser != null && tokenUser.getUsername() != null && tokenUser.getPassword() != null) {
            User user = userRepository.findByUsername(tokenUser.getUsername()).orElseThrow(RuntimeException::new);


            // Create login token
            authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            tokenUser.getUsername(), tokenUser.getPassword(), new ArrayList<>());
        }

        // Authenticate user
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) {

        try {
            UserPrincipal userPrincipal = (UserPrincipal) authResult.getPrincipal();
            String token =
                    JwtProperties.generateTokenFromPrincipal(userPrincipal, false, tokenUser.isRememberMe());

            String tokenrefresh =
                    JwtProperties.generateTokenFromPrincipal(userPrincipal, true, tokenUser.isRememberMe());

            // Add token in response
            response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
            response.addHeader(JwtProperties.HEADER_REFRESH, JwtProperties.TOKEN_PREFIX + tokenrefresh);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
