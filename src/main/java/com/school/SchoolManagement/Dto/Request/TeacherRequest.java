package com.school.SchoolManagement.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TeacherRequest {
    private Long id;

    private String teacherName;
    private Date dob;
    private String gender;
    private String address;
    private String phone;
    private String salary;
    private String specialization;
    private Boolean status;

    private Long steamId;
    private Long courseId;


    private String email;

}


