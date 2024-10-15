package com.school.SchoolManagement.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
    private Long id;
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

}
