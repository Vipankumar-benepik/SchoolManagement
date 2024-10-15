package com.school.SchoolManagement.Dto.Request;

import lombok.Data;

import java.util.Date;

@Data
public class AdminRequest {
    private String firstName;
    private String lastName;
    private String gender;
    private String address;
    private String phone;
    private Date dob;

    private String username;
    private String email;
    private String password;
}
