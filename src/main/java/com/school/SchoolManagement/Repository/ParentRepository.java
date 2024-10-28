package com.school.SchoolManagement.Repository;

import com.school.SchoolManagement.Entity.Parent;
import com.school.SchoolManagement.Utils.GenericRepository;

import java.util.List;

public interface ParentRepository extends GenericRepository<Parent, Long> {
    Parent findByEmail(String email);
    List<Parent> findByParentName(String username);
}
