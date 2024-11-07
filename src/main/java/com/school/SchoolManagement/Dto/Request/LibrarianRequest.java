package com.school.SchoolManagement.Dto.Request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LibrarianRequest {
    private Long id;

    @Column(name = "librarian_name")
    @NotNull(message = "Librarian Name cannot be empty")
    private String librarianName;

    @Column(name = "dob")
    @NotNull(message = "Dob cannot be empty")
    private Date dob;

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

    @Column(name = "salary")
    @NotNull(message = "Salary cannot be empty")
    private String salary;

    @Column(name = "hire_date")
    @NotNull(message = "Hire Date cannot be empty")
    private Date hireDate;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "email", unique = true, length = 50)
    @NotNull(message = "Email cannot be empty")
    @Email(message = "Email must be a valid email address")
    private String email;
}


