package com.school.SchoolManagement.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponse {
    private Long id;
    private String adminName;
    private String gender;
    private String address;
    private String phone;
    private Date dob;

    private String email;
}
