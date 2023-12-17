package com.qpassessment.controllers;

import com.qpassessment.entities.UserDetail;
import com.qpassessment.dtos.UserDetailDto;
import com.qpassessment.service.AuthenticationService;
import com.qpassessment.service.UserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Component
public class UserDetailController {

    private UserDetailsService userDetailsService;

    private AuthenticationService authenticationService;

    private ModelMapper modelMapper;

    @Autowired
    public UserDetailController(UserDetailsService userDetailsService, AuthenticationService authenticationService, ModelMapper modelMapper){
        this.userDetailsService=userDetailsService;
        this.authenticationService=authenticationService;
        this.modelMapper=modelMapper;
    }


    @GetMapping("/userdetail")
    public ResponseEntity getUser(HttpServletRequest request) throws Exception {
        UserDetailDto userDetailDto = authenticationService.authenticateToken(request);
        if(userDetailDto!=null){
            UserDetail user=userDetailsService.getUser(userDetailDto.getUsername());
            return ResponseEntity.ok(convertToDto(user));
        }
        else{
            return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/userdetail/{userId}")
    public ResponseEntity getUser(HttpServletRequest request, @PathVariable Long userId) throws Exception {
        UserDetailDto userDetailDto = authenticationService.authenticateToken(request);
        if(userDetailDto!=null && userDetailDto.getRole().equals(UserDetailDto.Role.ADMIN)){
            UserDetail user=userDetailsService.getUserById(userId);
            if(user!=null)
                return ResponseEntity.ok(convertToDto(user));
            else
                return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        else{
            return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/userdetails")
    public ResponseEntity getUsers(HttpServletRequest request) throws Exception {
        UserDetailDto userDetailDto = authenticationService.authenticateToken(request);
        if(userDetailDto!=null){
            if(userDetailDto.getRole().equals(UserDetailDto.Role.ADMIN)){
                List<UserDetail> users=userDetailsService.getUsers();
                List<UserDetailDto> userDetailDtos = users.stream().map(this::convertToDto).toList();
                return ResponseEntity.ok(userDetailDtos);
            }
        }
        return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/userdetail")
    public ResponseEntity updateUsers(HttpServletRequest request, @RequestBody @Valid UserDetailDto userDetailDto) throws Exception {
        UserDetailDto login_userDetailDto = authenticationService.authenticateToken(request);
        if(login_userDetailDto!=null && (userDetailDto.getUsername().equals(login_userDetailDto.getUsername()) || login_userDetailDto.getRole().equals(UserDetailDto.Role.ADMIN))){
            UserDetail userDetail = convertToEntity(userDetailDto);
            userDetail = userDetailsService.updateUser(userDetail);
            userDetailDto = convertToDto(userDetail);
            return ResponseEntity.ok(userDetailDto);
        }
        return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/userdetail/{userId}")
    public ResponseEntity deleteUsers(HttpServletRequest request, @PathVariable Long userId) throws Exception {
        UserDetailDto login_userDetailDto = authenticationService.authenticateToken(request);
        if(login_userDetailDto!=null && (login_userDetailDto.getRole().equals(UserDetailDto.Role.ADMIN))){
            userDetailsService.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully.");
        }
        return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
    }

    private UserDetailDto convertToDto(UserDetail user){
        return modelMapper.map(user,UserDetailDto.class);
    }

    private UserDetail convertToEntity(UserDetailDto userDetailDto){
        return modelMapper.map(userDetailDto,UserDetail.class);
    }
}
