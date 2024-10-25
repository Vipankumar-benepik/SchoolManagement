package com.school.SchoolManagement.Entity;

import jakarta.persistence.*;
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

    private String email;
    private String password;
    private Boolean status;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Long refId;
}


