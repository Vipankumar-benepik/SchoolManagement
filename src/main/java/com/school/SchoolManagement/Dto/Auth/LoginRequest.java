package com.school.SchoolManagement.Dto.Auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
