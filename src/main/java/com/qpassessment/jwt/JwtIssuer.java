package com.qpassessment.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class JwtIssuer {

    @Autowired
    private Environment environment;
    public String issue(String username, String role){
        return JWT.create()
                .withSubject(username)
                .withClaim("role",role)
                .withExpiresAt(Instant.now().plus(Duration.ofMinutes(300)))
                .sign(Algorithm.HMAC512(environment.getProperty("secret-key").getBytes()));
    }

}
