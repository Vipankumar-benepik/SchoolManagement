package com.school.SchoolManagement.Dto.Request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookLoanRequest {

    @Column(name = "student_id")
    @NotNull(message = "StudentId cannot be empty")
    private Long studentId;

    @Column(name = "book_id")
    @NotNull(message = "BookId cannot be empty")
    private Long bookId;
}
