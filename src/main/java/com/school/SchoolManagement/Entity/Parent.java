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
public class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_name")
    @NotNull(message = "Parent Name cannot be empty")
    private String parentName;

    @Column(name = "dob")
    @NotNull(message = "Dob cannot be empty")
    private Date dob;

    @Column(name = "gender")
    @NotNull(message = "Gender cannot be empty")
    private String gender;

    @Column(name = "relation")
    @NotNull(message = "Relation cannot be empty")
    private String relation;

    @Column(name = "address")
    @NotNull(message = "Address cannot be empty")
    private String address;

    @NotNull(message = "Phone number cannot be empty")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be a valid phone number with 10 to 15 digits")
    @Column(name = "phone")
    private String phone;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "email", unique = true, length = 50)
    @NotNull(message = "Email cannot be empty")
    @Email(message = "Email must be a valid email address")
    @Size(max = 50, message = "Address cannot exceed 50 characters")
    private String email;
}
