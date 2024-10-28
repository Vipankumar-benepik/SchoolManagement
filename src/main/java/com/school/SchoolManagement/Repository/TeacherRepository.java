package com.school.SchoolManagement.Repository;

import com.school.SchoolManagement.Entity.Teacher;
import com.school.SchoolManagement.Utils.GenericRepository;

import java.util.List;

public interface TeacherRepository extends GenericRepository<Teacher, Long> {
    Teacher findByEmail(String email);
    List<Teacher> findByTeacherName(String username);
}
