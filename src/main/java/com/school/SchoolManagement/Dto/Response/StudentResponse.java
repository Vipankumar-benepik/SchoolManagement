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

    private String studentName;
    private Integer standard;
    private Date dob;
    private Date admissionYear;
    private Date enrollmentDate;
    private String gender;
    private String address;
    private String phone;
    private Float gradeLevel;
    private Boolean status;

    private Long parentId;

    private String email;

}
