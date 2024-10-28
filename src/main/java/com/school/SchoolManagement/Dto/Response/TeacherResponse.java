package com.school.SchoolManagement.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TeacherResponse {
    private Long id;
    
    private String teacherName;
    private Date dob;
    private String gender;
    private String address;
    private String phone;
    private String salary;
    private String specialization;
    private Date hireDate;

    private Long departmentId;
    private Long courseId;


    private String email;
}
