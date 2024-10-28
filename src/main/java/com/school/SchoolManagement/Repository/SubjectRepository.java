package com.school.SchoolManagement.Repository;

import com.school.SchoolManagement.Entity.Subject;
import com.school.SchoolManagement.Utils.GenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends GenericRepository<Subject, Long> {
    Subject findBySubjectName(String subjectName);
}
