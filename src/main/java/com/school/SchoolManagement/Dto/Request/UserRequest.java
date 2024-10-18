package com.school.SchoolManagement.Dto.Request;

import com.school.SchoolManagement.Entity.Role;
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
    private Role role;
    private Long refId;
}
