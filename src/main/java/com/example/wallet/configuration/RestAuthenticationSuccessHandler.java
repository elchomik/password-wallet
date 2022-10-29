package com.example.wallet.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.wallet.readonly.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final long expirationTime;
    private final String secret;

    public RestAuthenticationSuccessHandler(
            final @Value("${jwt.expirationTime}") long expirationTime,
            final @Value("${jwt.secret}") String secret) {
        this.expirationTime = expirationTime;
        this.secret = secret;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        final AuthenticatedUser principal = (AuthenticatedUser) authentication.getPrincipal();
       final  String token = JWT.create()
                .withSubject(principal.getAuthenitactedUserData().getLogin())
                .withExpiresAt(new Date(System.currentTimeMillis()+ expirationTime))
                .sign(Algorithm.HMAC512(secret));
        response.addHeader("Authorization", "Bearer "+token);

    }

}
