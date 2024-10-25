package com.school.SchoolManagement.Dto.Request;

import com.school.SchoolManagement.Entity.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRequest {
    private Long id;
    private String email;
    private String password;
    private Boolean status;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Long refId;
}
