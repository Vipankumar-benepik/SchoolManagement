package com.school.SchoolManagement.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FeesResponse {
    private Long id;

    private String feeType; // E.g., "Tuition Fee"
    private BigDecimal amount;
    private BigDecimal paidAmount = BigDecimal.ZERO;
    private Date paymentDate;
    private Boolean paymentStatus = false;
    private String month; // E.g., "2024-10" to represent the payment month

    private Long studentId;
}
