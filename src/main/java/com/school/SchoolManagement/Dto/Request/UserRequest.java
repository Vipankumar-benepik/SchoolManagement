package com.school.SchoolManagement.Dto.Request;

import com.school.SchoolManagement.Entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRequest {
    private Long id;

    @Column(name = "email")
    @NotNull(message = "Email cannot be empty")
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
