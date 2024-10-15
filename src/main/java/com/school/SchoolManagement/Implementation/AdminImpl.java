package com.school.SchoolManagement.Implementation;

import com.school.SchoolManagement.Dto.Request.AdminRequest;
import com.school.SchoolManagement.Dto.Response.AdminResponse;

import java.util.List;

public interface AdminImpl {
    List<AdminResponse> findAllAdmin();
    AdminResponse findById(Long id);
    AdminResponse createAdmin(AdminRequest adminRequest);
    void updateAdmin(Long id, AdminRequest adminRequest);
    void deleteAdmin(Long id);
}
