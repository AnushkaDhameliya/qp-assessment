package com.qpassessment.service;

import com.qpassessment.entities.UserDetail;

import java.util.List;

public interface UserDetailsService {

    UserDetail getUser(String username);

    UserDetail getUserById(Long userId);

    List<UserDetail> getUsers();

    UserDetail updateUser(UserDetail userDetail);

    void deleteUser(Long userId);
}
