package com.school.SchoolManagement.Implementation;

import com.school.SchoolManagement.Dto.Request.TeacherRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;

import java.util.List;

public interface TeacherImpl {
    BaseApiResponse findAllTeacher();
    BaseApiResponse findById(Long id);
    BaseApiResponse findByEmail(String email);
    BaseApiResponse findByTeacherName(String username);
    BaseApiResponse createOrUpdateTeacher(TeacherRequest teacherRequest);
    BaseApiResponse deleteTeacher(Long id);
    BaseApiResponse deleteTeacherByEmail(String username);
}
