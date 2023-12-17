package com.qpassessment.service;

import com.qpassessment.entities.UserDetail;
import com.qpassessment.dtos.UserDetailDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    public UserDetail login(String username, String password) throws Exception;

    public UserDetail register(UserDetail user);

    public String getJwtToken(String username, String role);

    public UserDetailDto authenticateToken(HttpServletRequest request) throws Exception;
}
