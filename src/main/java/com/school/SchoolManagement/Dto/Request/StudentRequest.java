package com.school.SchoolManagement.Dto.Request;

import lombok.Data;

import java.util.Date;

@Data
public class StudentRequest {
    private String firstName;
    private String lastName;
    private Date dob;
    private Date enrollmentDate;
    private String gender;
    private String address;
    private String phone;
    private Float gradeLevel;

    private String username;
    private String email;
    private String password;
}
