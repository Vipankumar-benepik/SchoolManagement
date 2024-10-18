package com.school.SchoolManagement.Repository;

import com.school.SchoolManagement.Dto.Response.AdminResponse;
import com.school.SchoolManagement.Entity.Admin;
import com.school.SchoolManagement.Utils.GenericRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends GenericRepository<Admin, Long> {
    Admin findByEmail(String email);
    List<Admin> findByAdminName(String username);
}
