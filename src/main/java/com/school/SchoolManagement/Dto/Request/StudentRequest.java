package com.school.SchoolManagement.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequest {
    private Long id;

    private String studentName;
    private Date dob;
    private Date enrollmentDate;
    private String gender;
    private String address;
    private String phone;
    private Float gradeLevel;
    private Boolean status;

    private String email;
}
