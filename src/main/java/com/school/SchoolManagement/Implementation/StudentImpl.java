package com.school.SchoolManagement.Implementation;

import com.school.SchoolManagement.Dto.Request.StudentRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;

import java.util.List;

public interface StudentImpl {
    BaseApiResponse findAllStudent();
    BaseApiResponse findById(Long id);
    BaseApiResponse findByEmail(String email);
    BaseApiResponse findByStudentName(String username);
    BaseApiResponse createOrUpdateStudent(StudentRequest studentRequest);
    BaseApiResponse createMultiple(List<StudentRequest> studentRequests);
    BaseApiResponse deleteStudent(Long id);
    BaseApiResponse deleteStudentByEmail(String username);
}
