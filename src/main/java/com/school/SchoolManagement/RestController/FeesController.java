package com.school.SchoolManagement.RestController;

import com.school.SchoolManagement.Dto.Request.FeesRequest;
import com.school.SchoolManagement.Dto.Request.SearchRequestDto.CommonRequestDto;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Implementation.FeesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

import static com.school.SchoolManagement.Constrants.RestMappingConstraints.*;

@RestController
@RequestMapping(BASE_URL)
public class FeesController {
    @Autowired
    private FeesImpl feesImpl;

    @PostMapping(DEFINE_API.FEES_PAYMENT_FETCH_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'PARENT', 'LIBRARIAN', 'STUDENT')")
    public ResponseEntity<BaseApiResponse> getPaymentDetailsById(@RequestBody CommonRequestDto requestDto) {
        try {
            if(requestDto.getId() == null || requestDto.getId() <= 0){
                BaseApiResponse errorResponse = new BaseApiResponse(STATUS_CODES.HTTP_BAD_REQUEST, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.FIELD_REQUIRED_MESSAGE, Collections.emptyList());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }

            BaseApiResponse baseApiResponse = feesImpl.findByStudentId(Long.valueOf(requestDto.getId()));
            return ResponseEntity.status(HttpStatus.OK).body(baseApiResponse);

        } catch (Exception e) {
            if (e.getMessage().equals("Data not Found.")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList()));
            }
            BaseApiResponse errorResponse = new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(DEFINE_API.FEES_PAYMENT_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'PARENT')")
    public ResponseEntity<BaseApiResponse> makePayment(@RequestBody FeesRequest feesRequest) {
        try {
            if (feesRequest.getStudentId() == null || feesRequest.getStudentId() <= 0) {
                BaseApiResponse response = new BaseApiResponse(STATUS_CODES.HTTP_BAD_REQUEST, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.FIELD_REQUIRED_MESSAGE, Collections.emptyList());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            BaseApiResponse response = feesImpl.makePayment(feesRequest.getStudentId(), feesRequest.getPaidAmount(), feesRequest.getFeeType());

            if (response.getSuccess() == 1) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            BaseApiResponse errorResponse = new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}
