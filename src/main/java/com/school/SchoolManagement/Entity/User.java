package com.school.SchoolManagement.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = {"role", "refId"}))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "email")
    @NotNull(message = "Email cannot be empty")
    @Email(message = "Email must be a valid email address")
    @Size(max = 50, message = "Address cannot exceed 50 characters")
    private String email;

    @Column(name = "password")
    @NotNull(message = "Password cannot be empty")
    private String password;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "role")
    @NotNull(message = "Role cannot be empty")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "refId")
    @NotNull(message = "RefId cannot be empty")
    private Long refId;
}


