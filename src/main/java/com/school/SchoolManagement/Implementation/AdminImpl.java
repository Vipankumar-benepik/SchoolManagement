package com.school.SchoolManagement.Implementation;

import com.school.SchoolManagement.Dto.Request.AdminRequest;
import com.school.SchoolManagement.Dto.Response.AdminResponse;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Entity.Admin;

import java.util.List;

public interface AdminImpl {
    BaseApiResponse findAllAdmin();
    BaseApiResponse findById(Long id);
    BaseApiResponse findByEmail(String email);
    BaseApiResponse findByAdminName(String username);
    BaseApiResponse createOrUpdateAdmin(AdminRequest adminRequest);
    BaseApiResponse createMultiple(List<AdminRequest> adminRequests);
    BaseApiResponse deleteAdmin(Long id);
    BaseApiResponse deleteAdminByEmail(String username);
}
