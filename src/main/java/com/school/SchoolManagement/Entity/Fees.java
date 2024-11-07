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
public class Fees {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "feeType")
//    @NotNull(message = "FeeType cannot be empty")
    private String feeType; // E.g., "Tuition Fee"

    @Column(name = "amount")
//    @NotNull(message = "Amount cannot be empty")
    private BigDecimal amount;

    @Column(name = "paid_amount")
//    @NotNull(message = "Paid Amount cannot be empty")
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(name = "payment_date")
//    @NotNull(message = "Payment Date cannot be empty")
    private Date paymentDate;

    @Column(name = "payment_status")
//    @NotNull(message = "Payment Status cannot be empty")
    private Boolean paymentStatus = false;

    @Column(name = "month")
//    @NotNull(message = "Month cannot be empty")
    private String month; // E.g., "2024-10" to represent the payment month

    @Column(name = "student_id")
//    @NotNull(message = "StudentId cannot be empty")
    private Long studentId;
}
