package com.school.SchoolManagement.Dto.Request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRequest {

    private Long id;

    @NotEmpty(message = "Admin name cannot be Empty")
    @Size(max = 50, message = "Cannot exceed 50 characters")
    @Column(name = "adminName", nullable = false, length = 20)
    private String adminName;

    @NotNull(message = "Gender cannot be null")
    @Pattern(regexp = "^(Male|Female)$", message = "Gender must be either 'Male' or 'Female'")
    @Column(name = "gender")
    private String gender;

    @NotEmpty(message = "Admin address cannot be Empty")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    @Column(name = "address")
    private String address;

    @NotEmpty(message = "Phone number cannot be null")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be a valid phone number with 10 to 15 digits")
    @Column(name = "phone")
    private String phone;

    @NotNull(message = "Date of birth cannot be null")
    @Past(message = "Date of birth must be in the past")
    @Column(name = "dob")
    private Date dob;

    @NotNull(message = "Status cannot be null")
    @Column(name = "status")
    private Boolean status;

    @NotEmpty(message = "Email cannot be null")
    @Size(max = 50, message = "Address cannot exceed 50 characters")
    @Email(message = "Email must be a valid email address")
    @Column(name = "email")
    private String email;
}
