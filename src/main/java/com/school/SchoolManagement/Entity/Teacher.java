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

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "teacher_name")
    @NotNull(message = "Teacher Name cannot be null")
    private String teacherName;

    @Column(name = "dob")
    @NotNull(message = "Dob cannot be null")
    private Date dob;

    @Column(name = "gender")
    @NotNull(message = "Gender cannot be null")
    private String gender;

    @Column(name = "address")
    @NotNull(message = "Address cannot be null")
    private String address;

    @NotNull(message = "Phone number cannot be empty")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be a valid phone number with 10 to 15 digits")
    @Column(name = "phone")
    private String phone;

    @Column(name = "fines")
    @NotNull(message = "Salary cannot be empty")
    private String salary;

    @Column(name = "specialization")
    @NotNull(message = "Specialization cannot be empty")
    private String specialization;

    @Column(name = "hire_date")
    @NotNull(message = "Hire Date cannot be empty")
    private Date hireDate;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "stream_id")
    @NotNull(message = "StreamId cannot be empty")
    private Long streamId;

    @Column(name = "email", unique = true, length = 50)
    @NotNull(message = "Email cannot be empty")
    @Email(message = "Email must be a valid email address")
    @Size(max = 50, message = "Address cannot exceed 50 characters")
    private String email;
}
