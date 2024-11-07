package com.school.SchoolManagement.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookLoanResponse {
    private Long id;

    private BigDecimal fines;
    private Date borrowDate;
    private Date returnDate;

    private Long studentId;
    private Long bookId;
}
