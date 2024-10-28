package com.school.SchoolManagement.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ParentRequest {
    private Long id;

    private String parentName;
    private Date dob;
    private String gender;
    private String relation;
    private String address;
    private String phone;
    private Boolean status;

    private Long studentId;

    private String email;
}


