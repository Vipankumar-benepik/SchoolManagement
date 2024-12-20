package com.school.SchoolManagement.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LibrarianResponse {
    private Long id;

    private String librarianName;
    private Date dob;
    private String gender;
    private String address;
    private String phone;
    private String salary;
    private Date hireDate;

    private String email;
}
