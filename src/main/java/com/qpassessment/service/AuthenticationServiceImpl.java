package com.qpassessment.service;

import com.qpassessment.entities.UserDetail;
import com.qpassessment.jwt.JwtDecoder;
import com.qpassessment.jwt.JwtIssuer;
import com.qpassessment.dtos.UserDetailDto;
import com.qpassessment.repositories.UserDetailRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@Component
public class AuthenticationServiceImpl implements AuthenticationService{

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Autowired
    private JwtIssuer jwtIssuer;

    @Autowired
    private  JwtDecoder jwtDecoder;

    @Override
    public UserDetail login(String username, String password) throws  Exception{
        UserDetail userDetail= userDetailRepository.findByUsername(username);
        if(userDetail==null){
            throw new Exception("Username is incorrect.");
        }
        if(!userDetail.getPassword().equals(password)){
            throw new Exception("Password is incorrect.");
        }
        userDetail.setLastLoginDate(new Timestamp(System.currentTimeMillis()));
        userDetailRepository.saveAndFlush(userDetail);
        return userDetail;
    }

    @Override
    public UserDetail register(UserDetail user) {
        return userDetailRepository.save(user);
    }

    @Override
    public String getJwtToken(String username, String role) {
        return jwtIssuer.issue(username, role);
    }


    @Override
    public UserDetailDto authenticateToken(HttpServletRequest request) throws Exception{
        UserDetailDto userDetailDto=jwtDecoder.authenticate(request);
        return userDetailDto;
    }
}
