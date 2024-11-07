package com.school.SchoolManagement.RestController;

import ch.qos.logback.core.util.StringUtil;
import com.school.SchoolManagement.Dto.Request.ParentRequest;
import com.school.SchoolManagement.Dto.Request.SearchRequestDto.SearchRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Implementation.ParentImpl;
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
import java.util.List;

import static com.school.SchoolManagement.Constrants.RestMappingConstraints.*;

@RestController
@RequestMapping(BASE_URL)
public class ParentController {

    @Autowired
    private ParentImpl parentImpl;

    @Autowired
    private CommonUtils commonUtils;


    @PostMapping(DEFINE_API.PARENT_FETCH_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER' ,'PARENT')")
    public ResponseEntity<BaseApiResponse> getAll() {
        try {
            BaseApiResponse parents = parentImpl.findAllParent();
            if (parents.getSuccess() == 1) {
                return ResponseEntity.status(HttpStatus.OK).body(parents);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(parents);
            }
        } catch (Exception e) {
            BaseApiResponse errorResponse = new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(DEFINE_API.PARENT_FETCH_BY_ID_API)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseApiResponse> getById(@RequestBody SearchRequest searchRequest) {
        try {
            if (StringUtil.isNullOrEmpty(searchRequest.getEmail()) && StringUtil.isNullOrEmpty(searchRequest.getName()) && searchRequest.getId() == null) {
                BaseApiResponse baseApiResponse = new BaseApiResponse(STATUS_CODES.HTTP_BAD_REQUEST, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.FIELD_REQUIRED_MESSAGE, Collections.emptyList());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseApiResponse);
            }

            BaseApiResponse baseApiResponse = new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
            if (searchRequest.getId() != null && searchRequest.getId() != 0) {
                baseApiResponse = parentImpl.findById(searchRequest.getId());
            } else if (searchRequest.getEmail() != null && !StringUtil.isNullOrEmpty(searchRequest.getEmail())) {
                baseApiResponse = parentImpl.findByEmail(searchRequest.getEmail());
            } else if (!StringUtil.isNullOrEmpty(searchRequest.getName())) {
                baseApiResponse = parentImpl.findByParentName(searchRequest.getName());
            } else if (searchRequest.getParentId()>0 && searchRequest.getId() != null){
                baseApiResponse = parentImpl.findByStudentsParentId(searchRequest.getParentId());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse(STATUS_CODES.HTTP_NOT_ACCEPTABLE, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.REQUEST_NOT_ACCEPTABLE, Collections.emptyList()));
            }

            if (baseApiResponse.getSuccess() == 1) {
                return ResponseEntity.status(HttpStatus.OK).body(baseApiResponse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseApiResponse);
            }
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Parent not Found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList()));
            }
            BaseApiResponse errorResponse = new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(DEFINE_API.PARENT_CREATE_API)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseApiResponse> createOrUpdate(@RequestBody ParentRequest request) {
        try {
            if (StringUtil.isNullOrEmpty(request.getEmail()) || StringUtil.isNullOrEmpty(request.getParentName()) || request.getParentName().length() < 3 || !commonUtils.isValidEmail(request.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseApiResponse(STATUS_CODES.HTTP_BAD_REQUEST, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.INVALID_REQUEST, Collections.emptyList()));
            }
            BaseApiResponse baseApiResponse = parentImpl.createOrUpdateParent(request);
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

    @PostMapping(DEFINE_API.PARENT_BATCH_API)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseApiResponse> createBatch(@RequestBody List<ParentRequest> requests) {
        try {
            BaseApiResponse baseApiResponse = parentImpl.createMultiple(requests);
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

    @PostMapping(DEFINE_API.PARENT_DELETE_API)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseApiResponse> delete(@RequestBody SearchRequest searchRequest) {
        try {
            BaseApiResponse baseApiResponse = new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
            if (searchRequest.getId() != null && searchRequest.getId() != 0) {
                baseApiResponse = parentImpl.deleteParent(searchRequest.getId());
            } else if (!StringUtil.isNullOrEmpty(searchRequest.getEmail()) && commonUtils.isValidEmail(searchRequest.getEmail())) {
                baseApiResponse = parentImpl.deleteParentByEmail(searchRequest.getEmail());
            }
            if (baseApiResponse.getSuccess() == 1) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(baseApiResponse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList()));
            }
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Parent not Found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList()));
            }
            BaseApiResponse errorResponse = new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
