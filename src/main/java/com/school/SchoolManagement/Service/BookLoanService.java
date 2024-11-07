package com.school.SchoolManagement.Service;

import com.school.SchoolManagement.Dto.Request.BookLoanRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Dto.Response.BookLoanResponse;
import com.school.SchoolManagement.Entity.Book;
import com.school.SchoolManagement.Entity.BookLoan;
import com.school.SchoolManagement.Entity.Student;
import com.school.SchoolManagement.Implementation.BookLoanImpl;
import com.school.SchoolManagement.Repository.BookLoanRepository;
import com.school.SchoolManagement.Repository.LibraryRepository;
import com.school.SchoolManagement.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static com.school.SchoolManagement.Constrants.RestMappingConstraints.*;

@Service
public class BookLoanService implements BookLoanImpl {

    @Autowired
    private BookLoanRepository bookLoanRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private LibraryRepository libraryRepository;

    @Override
    public BaseApiResponse fetchBookLoan(Long id) {
        try {
            BookLoan bookLoan = bookLoanRepository.findById(id).orElseThrow(() -> new RuntimeException("Data not Found."));

            if (bookLoan == null) {
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.BOOKLOAN_FETCHED, Collections.emptyList());
            }
            return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.BOOKLOAN_FETCHED, bookLoan);
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse createBookLoan(BookLoanRequest request) {
        try {
            Optional<Student> student = studentRepository.findById(request.getStudentId());
            Optional<Book> book = libraryRepository.findById(request.getBookId());

            if (student.isPresent() && book.isPresent()) {
                // Ensure the book isn't already borrowed by the same student
                BookLoan existingLoan = bookLoanRepository.findByStudentIdAndBookId(request.getStudentId(), request.getBookId());

                if (existingLoan != null && existingLoan.getReturnDate() == null) {
                    return new BaseApiResponse(STATUS_CODES.HTTP_BAD_REQUEST, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.BOOK_ALREADY_RETURN, Collections.emptyList());
                }

                // Create and save a new loan
                BookLoan newLoan = new BookLoan();
                newLoan.setStudentId(request.getStudentId());
                newLoan.setBookId(request.getBookId());
                newLoan.setBorrowDate(new Date());

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(newLoan.getBorrowDate());
                calendar.add(Calendar.DAY_OF_YEAR, 14);
                newLoan.setReturnDate(calendar.getTime());

                BookLoan savedLoan = bookLoanRepository.save(newLoan);
                BookLoanResponse bookLoanResponse = mapToResponse(savedLoan);
                return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.BOOK_BORROWED, bookLoanResponse);
            } else {
                if (student.isEmpty()) {
                    return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.STUDENT_NOT_FOUND, Collections.emptyList());
                }
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.BOOK_NOT_FOUND, Collections.emptyList());
            }
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse returnBook(Long loanId) {
        try {
            BookLoan loan = bookLoanRepository.findById(loanId).orElseThrow(() -> new RuntimeException("Loan not found."));

            if (loan.getReturnDate() != null) {
                throw new IllegalStateException();
            }

            loan.setReturnDate(new Date());

            // Calculate fines (example: $5/day after 14 days)
            long overdueDays = (loan.getReturnDate().getTime() - loan.getBorrowDate().getTime()) / (1000 * 60 * 60 * 24);
            if (overdueDays > 14) {
                loan.setFines(BigDecimal.valueOf((overdueDays - 14) * 5));
            }

            BookLoan savedLoan = bookLoanRepository.save(loan);
            BookLoanResponse bookLoanResponse = mapToResponse(savedLoan);
            return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.BOOK_RETURNED, bookLoanResponse);
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }

    }

    private BookLoanResponse mapToResponse(BookLoan loan) {
        BookLoanResponse response = new BookLoanResponse();
        response.setId(loan.getId());
        response.setFines(loan.getFines());
        response.setBorrowDate(loan.getBorrowDate());
        response.setReturnDate(loan.getReturnDate());
        response.setStudentId(loan.getStudentId());
        response.setBookId(loan.getBookId());
        return response;
    }
}

