package com.school.SchoolManagement.RestController;

import ch.qos.logback.core.util.StringUtil;
import com.school.SchoolManagement.Dto.Request.SearchRequestDto.SearchRequest;
import com.school.SchoolManagement.Dto.Request.TeacherRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Implementation.TeacherImpl;
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
public class TeacherController {

    @Autowired
    private TeacherImpl teacherImpl;

    @Autowired
    private CommonUtils commonUtils;

    @PostMapping(DEFINE_API.TEACHER_FETCH_API)
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<BaseApiResponse> getAll() {
        try {
            BaseApiResponse teachers = teacherImpl.findAllTeacher();
            if (teachers.getSuccess() == 1) {
                return ResponseEntity.status(HttpStatus.OK).body(teachers);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(teachers);
            }
        } catch (Exception e) {
            BaseApiResponse errorResponse = new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(DEFINE_API.TEACHER_FETCH_BY_ID_API)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseApiResponse> getById(@RequestBody SearchRequest searchRequest) {
        try {
            if (StringUtil.isNullOrEmpty(searchRequest.getEmail()) && StringUtil.isNullOrEmpty(searchRequest.getName()) && searchRequest.getId() == null ) {
                BaseApiResponse baseApiResponse = new BaseApiResponse(STATUS_CODES.HTTP_BAD_REQUEST, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.FIELD_REQUIRED_MESSAGE, Collections.emptyList());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseApiResponse);
            }

            BaseApiResponse baseApiResponse = new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
            if (searchRequest.getId() != null && searchRequest.getId() != 0) {
                baseApiResponse = teacherImpl.findById(searchRequest.getId());
            } else if (searchRequest.getEmail() != null && !StringUtil.isNullOrEmpty(searchRequest.getEmail())) {
                baseApiResponse = teacherImpl.findByEmail(searchRequest.getEmail());
            } else if (!StringUtil.isNullOrEmpty(searchRequest.getName())) {
                baseApiResponse = teacherImpl.findByTeacherName(searchRequest.getName());
            }

            if (baseApiResponse.getSuccess() == 1) {
                return ResponseEntity.status(HttpStatus.OK).body(baseApiResponse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseApiResponse);
            }
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Teacher not Found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList()));
            }
            BaseApiResponse errorResponse = new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(DEFINE_API.TEACHER_CREATE_API)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseApiResponse> createOrUpdate(@RequestBody TeacherRequest request) {
        try {
            if (StringUtil.isNullOrEmpty(request.getEmail()) || StringUtil.isNullOrEmpty(request.getTeacherName()) || request.getTeacherName().length() < 3 || !commonUtils.isValidEmail(request.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseApiResponse(STATUS_CODES.HTTP_BAD_REQUEST, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.INVALID_REQUEST, Collections.emptyList()));
            }
            BaseApiResponse baseApiResponse = teacherImpl.createOrUpdateTeacher(request);
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

    @PostMapping(DEFINE_API.TEACHER_DELETE_API)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseApiResponse> delete(@RequestBody SearchRequest searchRequest) {
        try {
            BaseApiResponse baseApiResponse = new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
            if (searchRequest.getId() != null && searchRequest.getId() != 0) {
                baseApiResponse = teacherImpl.deleteTeacher(searchRequest.getId());
            } else if (!StringUtil.isNullOrEmpty(searchRequest.getEmail()) && commonUtils.isValidEmail(searchRequest.getEmail())) {
                baseApiResponse = teacherImpl.deleteTeacherByEmail(searchRequest.getEmail());
            }
            if (baseApiResponse.getSuccess() == 1) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(baseApiResponse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList()));
            }
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Teacher not Found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList()));
            }
            BaseApiResponse errorResponse = new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
