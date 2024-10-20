package com.school.SchoolManagement.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
