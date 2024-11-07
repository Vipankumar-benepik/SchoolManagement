package com.school.SchoolManagement.Implementation;

import com.school.SchoolManagement.Dto.Request.BookLoanRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;

public interface BookLoanImpl {
    BaseApiResponse fetchBookLoan(Long id);

    BaseApiResponse createBookLoan(BookLoanRequest request);
    BaseApiResponse returnBook(Long loanId);
}
