package com.school.SchoolManagement.Repository;

import com.school.SchoolManagement.Entity.BookLoan;
import com.school.SchoolManagement.Utils.GenericRepository;

public interface BookLoanRepository extends GenericRepository<BookLoan, Long> {
    BookLoan findByStudentIdAndBookId(Long studentId, Long bookId);
}
