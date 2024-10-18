package com.school.SchoolManagement.Repository;

import com.school.SchoolManagement.Entity.User;
import com.school.SchoolManagement.Utils.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends GenericRepository<User, Long> {
    User findByEmail(String email);
//    List<User> findByUserName(String username);
}
