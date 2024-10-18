package com.school.SchoolManagement.Dto.Response;

import com.school.SchoolManagement.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponse {
    private Long id;

    private String email;
    private Role role;
    private Long refId;
}
