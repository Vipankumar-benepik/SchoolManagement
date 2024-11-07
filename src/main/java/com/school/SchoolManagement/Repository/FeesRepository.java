package com.school.SchoolManagement.Repository;

import com.school.SchoolManagement.Entity.Fees;
import com.school.SchoolManagement.Utils.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeesRepository extends GenericRepository<Fees, Long> {
    Fees findByStudentId(Long id);
    Optional<Fees> findByStudentIdAndMonth(Long studentId, String month);
}
