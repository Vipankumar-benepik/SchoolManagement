package com.school.SchoolManagement.RestController;

import ch.qos.logback.core.util.StringUtil;
import com.school.SchoolManagement.Dto.Request.SearchRequestDto.StreamSearchRequest;
import com.school.SchoolManagement.Dto.Request.StreamRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Implementation.StreamImpl;
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
public class StreamController {

    @Autowired
    private StreamImpl streamImpl;

    @PostMapping(DEFINE_API.STREAM_FETCH_API)
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER', 'STUDENT', 'LIBRARIAN')")
    public ResponseEntity<BaseApiResponse> getAll() {
        try {
            BaseApiResponse streams = streamImpl.findAll();
            if (streams.getSuccess() == 1) {
                return ResponseEntity.status(HttpStatus.OK).body(streams);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(streams);
            }
        } catch (Exception e) {
            BaseApiResponse errorResponse = new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(DEFINE_API.STREAM_FETCH_BY_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    public ResponseEntity<BaseApiResponse> getById(@RequestBody StreamSearchRequest searchRequest) {
        try {
            if (StringUtil.isNullOrEmpty(searchRequest.getName()) && (searchRequest.getId() == null || searchRequest.getId() == 0) && searchRequest.getStreamHeadId() == null ) {
                BaseApiResponse baseApiResponse = new BaseApiResponse(STATUS_CODES.HTTP_BAD_REQUEST, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.FIELD_REQUIRED_MESSAGE, Collections.emptyList());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseApiResponse);
            }

            BaseApiResponse baseApiResponse = new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
            if (searchRequest.getId() != null && searchRequest.getId() != 0) {
                baseApiResponse = streamImpl.findById(searchRequest.getId());
            } else if (searchRequest.getName() != null && !StringUtil.isNullOrEmpty(searchRequest.getName())) {
                baseApiResponse = streamImpl.findByName(searchRequest.getName());
            } else if (searchRequest.getStreamHeadId() != null) {
                baseApiResponse = streamImpl.findByStreamHeadId(searchRequest.getStreamHeadId());
            }

            if (baseApiResponse.getSuccess() == 1) {
                return ResponseEntity.status(HttpStatus.OK).body(baseApiResponse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseApiResponse);
            }
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Stream not Found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList()));
            }
            BaseApiResponse errorResponse = new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(DEFINE_API.STREAM_CREATE_API)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseApiResponse> createOrUpdate(@RequestBody StreamRequest request) {
        try {
            if (StringUtil.isNullOrEmpty(request.getStreamName()) || request.getStreamName().length() < 3 || request.getStreamHeadId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseApiResponse(STATUS_CODES.HTTP_BAD_REQUEST, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.INVALID_REQUEST, Collections.emptyList()));
            }
            BaseApiResponse baseApiResponse = streamImpl.createOrUpdate(request);
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

    @PostMapping(DEFINE_API.STREAM_DELETE_API)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseApiResponse> delete(@RequestBody StreamSearchRequest searchRequest) {
        try {
            BaseApiResponse baseApiResponse = new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
            if (searchRequest.getId() != null && searchRequest.getId() != 0) {
                baseApiResponse = streamImpl.delete(searchRequest.getId());
            } else if (!StringUtil.isNullOrEmpty(searchRequest.getName()) ) {
                baseApiResponse = streamImpl.deleteByName(searchRequest.getName());
            }
            if (baseApiResponse.getSuccess() == 1) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(baseApiResponse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList()));
            }
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Stream not Found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList()));
            }
            BaseApiResponse errorResponse = new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}
