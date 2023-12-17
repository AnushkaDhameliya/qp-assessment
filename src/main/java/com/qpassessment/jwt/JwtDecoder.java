package com.qpassessment.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.qpassessment.dtos.UserDetailDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtDecoder {

    private final Environment environment;


    public DecodedJWT decode(String token){
        return JWT.require(Algorithm.HMAC512(environment.getProperty("secret-key").getBytes())).build().verify(token);
    }

    public UserDetailDto convert(DecodedJWT jwt){
        UserDetailDto userDetailDto=new UserDetailDto();
        userDetailDto.setUsername(jwt.getSubject());
        userDetailDto.setRole(UserDetailDto.Role.valueOf(jwt.getClaim("role").asString()));
        return userDetailDto;
    }

    protected Optional<String> extract(HttpServletRequest request){
        var token=request.getHeader("Authorization");
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")){
            return  Optional.of(token.substring(7));
        }
        return Optional.empty();
    }

    public  UserDetailDto authenticate(HttpServletRequest request) throws ServletException, IOException {
        Optional<String> token=extract(request);
        UserDetailDto userDetailDto=null;
        if(token.isPresent()){
            DecodedJWT decodedJWT=decode(token.get());
            userDetailDto=convert(decodedJWT);
        }

        return userDetailDto;
    }


}
