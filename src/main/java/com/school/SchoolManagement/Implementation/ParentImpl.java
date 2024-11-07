package com.school.SchoolManagement.Implementation;

import com.school.SchoolManagement.Dto.Request.ParentRequest;
import com.school.SchoolManagement.Dto.Request.StudentRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;

import java.util.List;

public interface ParentImpl {
    BaseApiResponse findAllParent();
    BaseApiResponse findById(Long id);
    BaseApiResponse findByStudentsParentId(Long id);
    BaseApiResponse findByEmail(String email);
    BaseApiResponse findByParentName(String username);
    BaseApiResponse createOrUpdateParent(ParentRequest parentRequest);
    BaseApiResponse createMultiple(List<ParentRequest> parentRequests);
    BaseApiResponse deleteParent(Long id);
    BaseApiResponse deleteParentByEmail(String username);
}
