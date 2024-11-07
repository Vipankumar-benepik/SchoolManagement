package com.school.SchoolManagement.Repository;

import com.school.SchoolManagement.Entity.Subject;
import com.school.SchoolManagement.Utils.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends GenericRepository<Subject, Long> {
    Subject findBySubjectName(String subjectName);
    List<Subject> findByStandard(Integer standardId);
}
