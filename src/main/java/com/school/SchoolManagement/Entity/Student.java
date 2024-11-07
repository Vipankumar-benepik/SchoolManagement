package com.school.SchoolManagement.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_name")
    @NotNull(message = "Student Name cannot be empty")
    private String studentName;

    @Column(name = "standard")
    @NotNull(message = "Standard cannot be empty")
    private Integer standard;

    @Column(name = "dob")
    @NotNull(message = "Dob cannot be empty")
    private Date dob;

    @Column(name = "admission_year")
    @NotNull(message = "Admission Year cannot be empty")
    private Date admissionYear;

    @Column(name = "enrollment_date")
    @NotNull(message = "Enrollment Date cannot be empty")
    private Date enrollmentDate;

    @Column(name = "gender")
    @NotNull(message = "Gender cannot be empty")
    private String gender;

    @Column(name = "address")
    @NotNull(message = "Address cannot be empty")
    private String address;

    @NotNull(message = "Phone number cannot be empty")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be a valid phone number with 10 to 15 digits")
    @Column(name = "phone")
    private String phone;

    @Column(name = "grade_level")
    @NotNull(message = "GradeLevel cannot be empty")
    private Float gradeLevel;

    @Column(name = "status")
    private Boolean status;


    //    private List<Subject> subjects;

    @Column(name = "parent_id")
    @NotNull(message = "ParentId cannot be empty")
    private Long parentId;

    @Column(name = "email", unique = true, length = 50)
    @NotNull(message = "Email cannot be empty")
    @Email(message = "Email must be a valid email address")
    @Size(max = 50, message = "Address cannot exceed 50 characters")
    private String email;
}
