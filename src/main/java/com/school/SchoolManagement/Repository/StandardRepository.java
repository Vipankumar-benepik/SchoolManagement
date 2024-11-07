package com.school.SchoolManagement.Repository;

import com.school.SchoolManagement.Entity.Standard;
import com.school.SchoolManagement.Entity.Stream;
import com.school.SchoolManagement.Utils.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StandardRepository extends GenericRepository<Standard, Long> {
    Standard findByStandardName(String standardName);
}
