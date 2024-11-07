package com.school.SchoolManagement.Implementation;

import com.school.SchoolManagement.Dto.Response.BaseApiResponse;

import java.math.BigDecimal;

public interface FeesImpl {
    BaseApiResponse makePayment(Long studentId, BigDecimal payment, String feeType);
    BaseApiResponse findByStudentId(Long studentId);
}
