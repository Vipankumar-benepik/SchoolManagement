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

    @NotNull(message = "Admin name cannot be Empty")
    @Size(max = 50, message = "Cannot exceed 50 characters")
    @Column(name = "adminName", nullable = false)
    private String adminName;

    @NotNull(message = "Gender cannot be empty")
    @Pattern(regexp = "^(Male|Female)$", message = "Gender must be either 'Male' or 'Female'")
    @Column(name = "gender")
    private String gender;

    @Size(max = 255, message = "Address cannot exceed 255 characters")
    @Column(name = "address")
    private String address;

    @NotNull(message = "Phone number cannot be empty")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be a valid phone number with 10 to 15 digits")
    @Column(name = "phone")
    private String phone;

    @NotNull(message = "Date of birth cannot be empty")
    @Past(message = "Date of birth must be in the past")
    @Column(name = "dob")
    private Date dob;

    @Column(name = "status")
    private Boolean status;

    @NotNull(message = "Email cannot be empty")
    @Size(max = 50, message = "Address cannot exceed 50 characters")
    @Email(message = "Email must be a valid email address")
    @Column(name = "email", unique = true, length = 50)
    private String email;

}
