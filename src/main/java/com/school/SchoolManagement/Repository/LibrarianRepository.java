package com.school.SchoolManagement.Repository;

import com.school.SchoolManagement.Entity.Librarian;
import com.school.SchoolManagement.Utils.GenericRepository;

import java.util.List;

public interface LibrarianRepository extends GenericRepository<Librarian, Long> {
    Librarian findByEmail(String email);
    List<Librarian> findByLibrarianName(String username);
}
