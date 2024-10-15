package com.school.SchoolManagement.Implementation;

import com.school.SchoolManagement.Dto.Request.StudentRequest;
import com.school.SchoolManagement.Dto.Response.StudentResponse;

import java.util.List;

public interface StudentImpl {
    List<StudentResponse> findAllStudent();
    StudentResponse findById(Long id);
    StudentResponse createStudent(StudentRequest studentRequest);
    void updateStudent(Long id, StudentRequest studentRequest);
    void deleteStudent(Long id);
}
