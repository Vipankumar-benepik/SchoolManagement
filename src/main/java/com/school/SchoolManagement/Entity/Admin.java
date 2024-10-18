package com.school.SchoolManagement.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotNull(message = "Admin name cannot be null")
//    @Size(max = 50, message = "Cannot exceed 50 characters")
//    @Column(name = "adminName", nullable = false, length = 100)
    private String adminName;

//    @NotNull(message = "Gender cannot be null")
//    @Pattern(regexp = "^(Male|Female)$", message = "Gender must be either 'Male' or 'Female'")
//    @Column(name = "gender")
    private String gender;

//    @Size(max = 255, message = "Address cannot exceed 255 characters")
//    @Column(name = "address")
    private String address;

//    @NotNull(message = "Phone number cannot be null")
//    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be a valid phone number with 10 to 15 digits")
//    @Column(name = "phone")
    private String phone;

//    @NotNull(message = "Date of birth cannot be null")
//    @Past(message = "Date of birth must be in the past")
//    @Column(name = "dob")
    private Date dob;

//    @NotNull(message = "Status cannot be null")
//    @Column(name = "status")
    private Boolean status;

//    @NotNull(message = "Email cannot be null")
//    @Size(max = 50, message = "Address cannot exceed 255 characters")
//    @Email(message = "Email must be a valid email address")
//    @Column(name = "email")
    private String email;

//    @NotNull(message = "Password cannot be null")
//    @Size(min = 6, message = "Password must be at least 6 characters long")
//    @Column(name = "password")
    private String password;
}
