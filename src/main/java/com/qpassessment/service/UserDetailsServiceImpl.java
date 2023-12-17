package com.qpassessment.service;

import com.qpassessment.entities.UserDetail;
import com.qpassessment.repositories.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Component
public class UserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    private UserDetailRepository userDetailRepository;

    public UserDetail getUser(String username){
        return userDetailRepository.findByUsername(username);
    }

    @Override
    public UserDetail getUserById(Long userId) {
        Optional<UserDetail> userDetail = userDetailRepository.findById(userId);
        if(userDetail.isPresent()){
            return userDetail.get();
        }
        return null;
    }

    @Override
    public List<UserDetail> getUsers() {
        return userDetailRepository.findAll();
    }

    @Override
    public UserDetail updateUser(UserDetail userDetail) {
        return userDetailRepository.save(userDetail);
    }

    @Override
    public void deleteUser(Long userId) {
        userDetailRepository.deleteById(userId);
    }
}
