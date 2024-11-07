package com.school.SchoolManagement.Implementation;

import com.school.SchoolManagement.Dto.Request.SubjectRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;

public interface SubjectImpl {
    BaseApiResponse findAll();
    BaseApiResponse findById(Long id);
    BaseApiResponse findByName(String subjectName);
    BaseApiResponse findByStandard(Integer standardId);
    BaseApiResponse createOrUpdate(SubjectRequest subjectRequest);
    BaseApiResponse delete(Long id);
    BaseApiResponse deleteByName(String subjectName);
}
