package com.school.SchoolManagement.RestController;

import ch.qos.logback.core.util.StringUtil;
import com.school.SchoolManagement.Dto.Request.SearchRequestDto.CommonRequestDto;
import com.school.SchoolManagement.Dto.Request.SearchRequestDto.SearchRequest;
import com.school.SchoolManagement.Dto.Request.StudentRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Implementation.StudentImpl;
import com.school.SchoolManagement.JWT.JwtService;
import com.school.SchoolManagement.Utils.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static com.school.SchoolManagement.Constrants.RestMappingConstraints.*;

@RestController
@RequestMapping(BASE_URL)
public class StudentController {
    @Autowired
    private StudentImpl studentImpl;

    @Autowired
    private CommonUtils commonUtils;

    @PostMapping(DEFINE_API.STUDENT_PROFILE_API)
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<BaseApiResponse> profile(HttpServletRequest request) {
        try {
            BaseApiResponse baseApiResponse = studentImpl.profile(request);
            return ResponseEntity.status(HttpStatus.OK).body(baseApiResponse); // Ensure to return the response entity
        } catch (Exception e) {
            BaseApiResponse errorResponse = new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(DEFINE_API.STUDENT_FETCH_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<BaseApiResponse> getAll(@RequestBody CommonRequestDto request) {
        try {
            if (request.getId() == 0 || request.getId() == null) {
                BaseApiResponse students = studentImpl.findAllStudent();
                if (students.getSuccess() == 1) {
                    return ResponseEntity.status(HttpStatus.OK).body(students);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(students);
                }
            } else {
                BaseApiResponse students = studentImpl.findByStandard(request.getId());
                if (students.getSuccess() == 1) {
                    return ResponseEntity.status(HttpStatus.OK).body(students);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(students);
                }
            }

        } catch (Exception e) {
            BaseApiResponse errorResponse = new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(DEFINE_API.STUDENT_FETCH_BY_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<BaseApiResponse> getById(@RequestBody SearchRequest searchRequest) {
        try {
            if (StringUtil.isNullOrEmpty(searchRequest.getEmail()) && StringUtil.isNullOrEmpty(searchRequest.getName()) && searchRequest.getId() == null) {
                BaseApiResponse baseApiResponse = new BaseApiResponse(STATUS_CODES.HTTP_BAD_REQUEST, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.FIELD_REQUIRED_MESSAGE, Collections.emptyList());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseApiResponse);
            }

            BaseApiResponse baseApiResponse = new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
            if (searchRequest.getId() != null && searchRequest.getId() != 0) {
                baseApiResponse = studentImpl.findById(searchRequest.getId());
            } else if (searchRequest.getEmail() != null && !StringUtil.isNullOrEmpty(searchRequest.getEmail())) {
                baseApiResponse = studentImpl.findByEmail(searchRequest.getEmail());
            } else if (!StringUtil.isNullOrEmpty(searchRequest.getName())) {
                baseApiResponse = studentImpl.findByStudentName(searchRequest.getName());
            }

            if (baseApiResponse.getSuccess() == 1) {
                return ResponseEntity.status(HttpStatus.OK).body(baseApiResponse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseApiResponse);
            }
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Student not Found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList()));
            }
            BaseApiResponse errorResponse = new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(DEFINE_API.STUDENT_CREATE_API)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseApiResponse> createOrUpdate(@RequestBody StudentRequest request) {
        try {
            if (StringUtil.isNullOrEmpty(request.getEmail()) || StringUtil.isNullOrEmpty(request.getStudentName()) || request.getStudentName().length() < 3 || !commonUtils.isValidEmail(request.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseApiResponse(STATUS_CODES.HTTP_BAD_REQUEST, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.INVALID_REQUEST, Collections.emptyList()));
            }
            BaseApiResponse baseApiResponse = studentImpl.createOrUpdateStudent(request);
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

    @PostMapping(DEFINE_API.STUDENT_BATCH_API)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseApiResponse> createBatch(@RequestBody List<StudentRequest> requests) {
        try {
            BaseApiResponse baseApiResponse = studentImpl.createMultiple(requests);
            if (baseApiResponse.getSuccess() == 1) {
                return ResponseEntity.status(HttpStatus.CREATED).body(baseApiResponse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(baseApiResponse);
            }
        } catch (RuntimeException e) {
            BaseApiResponse errorResponse = new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(DEFINE_API.STUDENT_DELETE_API)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseApiResponse> delete(@RequestBody SearchRequest searchRequest) {
        try {
            BaseApiResponse baseApiResponse = new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
            if (searchRequest.getId() != null && searchRequest.getId()>0) {
                baseApiResponse = studentImpl.deleteStudent(searchRequest.getId());
            } else if (!StringUtil.isNullOrEmpty(searchRequest.getEmail()) && commonUtils.isValidEmail(searchRequest.getEmail())) {
                baseApiResponse = studentImpl.deleteStudentByEmail(searchRequest.getEmail());
            }else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new BaseApiResponse(STATUS_CODES.HTTP_NOT_ACCEPTABLE, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.REQUEST_NOT_ACCEPTABLE, Collections.emptyList()));
            }

            if (baseApiResponse.getSuccess() == 1) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(baseApiResponse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList()));
            }

        } catch (RuntimeException e) {
            if (e.getMessage().equals("Student not Found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList()));
            }
            if (e.getMessage().equals("User not Found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList()));
            }
            BaseApiResponse errorResponse = new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
