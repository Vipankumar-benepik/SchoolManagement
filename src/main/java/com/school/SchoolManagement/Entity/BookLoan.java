package com.school.SchoolManagement.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fines")
    private BigDecimal fines;

    @Column(name = "borrow_date")
    @NotNull(message = "Gender cannot be empty")
    private Date borrowDate;

    @Column(name = "return_date")
    @NotNull(message = "Return Date cannot be empty")
    private Date returnDate;

    @Column(name = "student_id")
    @NotNull(message = "StudentId cannot be empty")
    private Long studentId;

    @Column(name = "book_id")
    @NotNull(message = "BookId cannot be empty")
    private Long bookId;
}
