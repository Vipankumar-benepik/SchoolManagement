package com.school.SchoolManagement.RestController;

import ch.qos.logback.core.util.StringUtil;
import com.school.SchoolManagement.Dto.Request.SearchRequestDto.SearchRequest;
import com.school.SchoolManagement.Dto.Request.UserRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Implementation.UserImpl;
import com.school.SchoolManagement.Utils.CommonUtils;
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
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    @Autowired
    private UserImpl userImpl;

    @Autowired
    private CommonUtils commonUtils;

    @PostMapping(DEFINE_API.USER_FETCH_API)
    public ResponseEntity<BaseApiResponse> getAll() {
        try {
            BaseApiResponse baseApiResponse = userImpl.findAllUser();
            if (baseApiResponse.getSuccess() == 1) {
                return ResponseEntity.status(HttpStatus.OK).body(baseApiResponse);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseApiResponse);
            }
        } catch (Exception e) {
            BaseApiResponse errorResponse = new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(DEFINE_API.USER_FETCH_BY_ID_API)
    public ResponseEntity<BaseApiResponse> getById(@RequestBody SearchRequest searchRequest) {
        try {
            if (StringUtil.isNullOrEmpty(searchRequest.getEmail()) && searchRequest.getId() == null) {
                BaseApiResponse baseApiResponse = new BaseApiResponse(STATUS_CODES.HTTP_BAD_REQUEST, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.FIELD_REQUIRED_MESSAGE, Collections.emptyList());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseApiResponse);
            }

            BaseApiResponse baseApiResponse = new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.USER_NOT_FOUND, Collections.emptyList());

            if (searchRequest.getId() != null && searchRequest.getId() != 0) {
                baseApiResponse = userImpl.findById(searchRequest.getId());
            } else if (searchRequest.getEmail() != null && !StringUtil.isNullOrEmpty(searchRequest.getEmail())) {
                baseApiResponse = userImpl.findByEmail(searchRequest.getEmail());
            }

            if (baseApiResponse.getSuccess() == 1) {
                return ResponseEntity.status(HttpStatus.OK).body(baseApiResponse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseApiResponse);
            }
        } catch (RuntimeException e) {
            if (e.getMessage().equals("User not Found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.USER_NOT_FOUND, Collections.emptyList()));
            }
            BaseApiResponse errorResponse = new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @PostMapping(DEFINE_API.USER_REGISTER_API)
    public ResponseEntity<BaseApiResponse> createOrUpdate(@RequestBody UserRequest request) {
        try {
            if (StringUtil.isNullOrEmpty(request.getEmail())  || !commonUtils.isValidEmail(request.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseApiResponse(STATUS_CODES.HTTP_BAD_REQUEST, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.INVALID_REQUEST, Collections.emptyList()));
            }
            BaseApiResponse baseApiResponse = userImpl.createOrUpdateUser(request);
            if (baseApiResponse.getSuccess() == 1) {
                if (request.getId() == null || request.getId() == 0) {
                    return ResponseEntity.status(HttpStatus.CREATED).body(baseApiResponse);
                } else {
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(baseApiResponse);
                }
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseApiResponse);
            }
        } catch (RuntimeException e) {
            BaseApiResponse errorResponse = new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(DEFINE_API.USER_DELETE_API)
    public ResponseEntity<BaseApiResponse> delete(@RequestBody SearchRequest searchRequest) {
        try {
            BaseApiResponse baseApiResponse = new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.USER_NOT_FOUND, Collections.emptyList());
            if (searchRequest.getId() != null && searchRequest.getId() >= 0) {
                baseApiResponse = userImpl.deleteUser(searchRequest.getId());
            } else if (!StringUtil.isNullOrEmpty(searchRequest.getEmail()) && commonUtils.isValidEmail(searchRequest.getEmail())) {
                baseApiResponse = userImpl.deleteUserByEmail(searchRequest.getEmail());
            }

            if (baseApiResponse.getSuccess() == 1) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(baseApiResponse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.USER_NOT_FOUND, Collections.emptyList()));
            }

        } catch (RuntimeException e) {
            if (e.getMessage().equals("User not Found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.USER_NOT_FOUND, Collections.emptyList()));
            }
            BaseApiResponse errorResponse = new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
