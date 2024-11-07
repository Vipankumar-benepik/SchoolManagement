package com.school.SchoolManagement.Service;

import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Entity.Fees;
import com.school.SchoolManagement.Entity.Student;
import com.school.SchoolManagement.Implementation.FeesImpl;
import com.school.SchoolManagement.Repository.FeesRepository;
import com.school.SchoolManagement.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;

import static com.school.SchoolManagement.Constrants.RestMappingConstraints.*;

import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Service
public class FeesService implements FeesImpl {

    @Autowired
    private FeesRepository feesRepository;

    @Autowired
    private StudentRepository studentRepository;

    private static final BigDecimal MONTHLY_FEE = BigDecimal.valueOf(1500);

    // Scheduled to run on the 1st day of every month at midnight
    @Scheduled(cron = "0 0 0 1 * ?")
    public void resetMonthlyFees() {
        List<Fees> allFees = feesRepository.findAll();
        String newMonth = new SimpleDateFormat("yyyy-MM").format(new Date());

        for (Fees fee : allFees) {
            fee.setPaidAmount(BigDecimal.ZERO);
            fee.setPaymentStatus(false);
            fee.setMonth(newMonth);
            feesRepository.save(fee);
        }
        System.out.println("Fees reset for a new month: " + newMonth);
    }

    @Override
    public BaseApiResponse makePayment(Long studentId, BigDecimal payment, String feeType) {
        try{
            // Validate the payment amount to ensure it is positive
            if (payment.compareTo(BigDecimal.ZERO) <= 0) {
                return new BaseApiResponse(STATUS_CODES.HTTP_BAD_REQUEST, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.INVALID_PAYMENT, "Payment must be a positive amount.");
            }

            Optional<Student> student = studentRepository.findById(studentId);

            if(student.isPresent()){
                // Find the current month’s fee for the student
                Fees fees = feesRepository.findByStudentIdAndMonth(studentId, getCurrentMonth())
                        .orElseGet(() -> createNewMonthlyFee(studentId)); // Create new fee if not found

                // Check if the payment exceeds the monthly fee
                if (fees.getPaidAmount().add(payment).compareTo(MONTHLY_FEE) > 0) {
                    return new BaseApiResponse(STATUS_CODES.HTTP_BAD_REQUEST, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.FEES_PAYMENT_INCOMPLETE, "Don't pay more than 1500");
                }

                // Update the paid amount
                fees.setPaidAmount(fees.getPaidAmount().add(payment));

                // Check if the full amount is paid
                if (fees.getPaidAmount().compareTo(MONTHLY_FEE) >= 0) {
                    fees.setPaymentStatus(true);
                    fees.setPaymentDate(new Date());
                }
                fees.setFeeType(feeType);
                fees.setPaymentDate(new Date());
                feesRepository.save(fees); // Save the updated fee record

                return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.FEES_PAYMENT_COMPLETED, fees);
            } else {
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.STUDENT_NOT_FOUND, Collections.emptyList());
            }

        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }

    }

    @Override
    public BaseApiResponse findByStudentId(Long studentId) {
        try {
            Fees fees = feesRepository.findByStudentId(studentId);

            if (fees != null) {
                return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.FEES_PAYMENT_FETCHED, fees);
            }
            return new BaseApiResponse(STATUS_CODES.HTTP_NOT_ACCEPTABLE, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.TRY_AGAIN, Collections.emptyList());
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    private String getCurrentMonth() {
        return new SimpleDateFormat("yyyy-MM").format(new Date());
    }

    private Fees createNewMonthlyFee(Long studentId) {
        Fees newFee = new Fees();
        newFee.setStudentId(studentId); // Set studentId directly
        newFee.setMonth(getCurrentMonth()); // Set the current month
        newFee.setAmount(MONTHLY_FEE); // Set the monthly fee amount
        newFee.setPaidAmount(BigDecimal.ZERO); // Initialize with zero paid amount
        newFee.setPaymentStatus(false); // Payment not yet completed
        newFee.setPaymentDate(null); // No payment date yet

        return feesRepository.save(newFee); // Save and return the new fee record
    }

}

