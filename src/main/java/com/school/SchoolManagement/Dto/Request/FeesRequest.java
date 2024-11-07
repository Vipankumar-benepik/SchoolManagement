package com.school.SchoolManagement.Dto.Request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FeesRequest {

    @Column(name = "feeType")
    @NotNull(message = "FeeType cannot be empty")
    private String feeType; // E.g., "Tuition Fee"

    @Column(name = "paid_amount")
    @NotNull(message = "Paid Amount cannot be empty")
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(name = "student_id")
    @NotNull(message = "StudentId cannot be empty")
    private Long studentId;
}
