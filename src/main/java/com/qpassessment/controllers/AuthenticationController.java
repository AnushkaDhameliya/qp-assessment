package com.qpassessment.controllers;

import com.qpassessment.entities.UserDetail;
import com.qpassessment.model.LoginRequest;
import com.qpassessment.model.LoginResponse;
import com.qpassessment.dtos.UserDetailDto;
import com.qpassessment.service.AuthenticationService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) throws Exception{
        UserDetail userDetail=authenticationService.login(request.getUsername(),request.getPassword());
        var token=authenticationService.getJwtToken(userDetail.getUsername(),userDetail.getRole().name());
        return ResponseEntity.ok(LoginResponse.builder().accessToken(token).build());
    }


    @PostMapping("register")
    public ResponseEntity<String> login(@RequestBody @Valid UserDetailDto userDetailDto) throws Exception{
        UserDetail user=convertToEntity(userDetailDto);
        UserDetail userDetail=authenticationService.register(user);
        if(userDetail!=null){
            return new ResponseEntity<>("User registered successfully. Try to login with username and password.",HttpStatus.OK);
        }
        return new ResponseEntity<>("Error encountered while registering.",HttpStatus.BAD_REQUEST);
    }

    private UserDetailDto convertToDto(UserDetail user){
        ModelMapper modelMapper=new ModelMapper();
        return modelMapper.map(user,UserDetailDto.class);
    }

    private UserDetail convertToEntity(UserDetailDto userDetailDto){
        ModelMapper modelMapper=new ModelMapper();
        return modelMapper.map(userDetailDto,UserDetail.class);
    }
}
