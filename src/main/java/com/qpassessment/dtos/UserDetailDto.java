package com.qpassessment.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailDto {

    public enum Role{
        USER,ADMIN;
    }

    private Long userId;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    @Size(min=6,max=35,message = "Username should have characters between 6 to 35.")
    private String username;

    private String password;

    @Email(message = "Email must be a well-formed email address.")
    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    private Role role;

    private Timestamp createdDate;

    private Timestamp lastLoginDate;
}
