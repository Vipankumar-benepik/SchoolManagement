package com.school.SchoolManagement.Repository;

import com.school.SchoolManagement.Dto.Response.StudentResponse;
import com.school.SchoolManagement.Entity.Admin;
import com.school.SchoolManagement.Entity.Student;
import com.school.SchoolManagement.Utils.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends GenericRepository<Student, Long> {
    Student findByEmail(String email);
    List<Student> findByStudentName(String username);
}
