package com.school.SchoolManagement.Service;

import com.school.SchoolManagement.Constrants.RestMappingConstraints;
import com.school.SchoolManagement.Dto.Request.SubjectRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Dto.Response.SubjectResponse;
import com.school.SchoolManagement.Entity.Subject;
import com.school.SchoolManagement.Implementation.SubjectImpl;
import com.school.SchoolManagement.Repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubjectService implements SubjectImpl {
    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public BaseApiResponse findAll() {
        try {
            List<Subject> subjects = subjectRepository.findAll();

            List<SubjectResponse> activeSubjects = subjects.stream()
                    .filter(subject -> subject.getStatus() != null && subject.getStatus())
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_OK, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.SUBJECT_FETCHED, activeSubjects);
        } catch (Exception e) {
            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse findById(Long id) {
        try {
            Subject subject = subjectRepository.findById(id).orElseThrow(() -> new RuntimeException("Subject not Found"));
            if (subject.getStatus()) {
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_OK, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.SUBJECT_FETCHED, subject);
            } else {
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_NOT_FOUND, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
            }
        } catch (Exception e) {
            if (e.getMessage().equals("Subject not Found")) {
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_NOT_FOUND, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
            }
            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse findByName(String subjectName) {
        try {
            Subject subject = subjectRepository.findBySubjectName(subjectName);
            if (subject != null && subject.getStatus()) {
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_OK, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.SUBJECT_FETCHED, subject);
            }
            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_NOT_FOUND, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
        } catch (Exception e) {
            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse createOrUpdate(SubjectRequest subjectRequest) {
        try {
            Subject subject = mapToEntity(subjectRequest);

            if (subjectRequest.getId() == null || subjectRequest.getId() == 0) {
                subjectRepository.save(subject);
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_CREATED, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.SUBJECT_CREATED, subject);
            } else {
                Optional<Subject> existingSubjectOpt = subjectRepository.findById(subject.getId());
                if (existingSubjectOpt.isPresent()) {
                    Subject existingSubject = existingSubjectOpt.get();
                    updateEntity(existingSubject, subjectRequest);
                    subjectRepository.save(existingSubject);
                    return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_ACCEPTED, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.SUBJECT_UPDATED, existingSubject);
                } else {
                    return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_NOT_FOUND, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
                }
            }
        } catch (Exception e) {
            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse delete(Long id) {
        try {
            Subject subject = subjectRepository.findById(id).orElseThrow(() -> new RuntimeException("Subject not Found"));
            if (subject.getStatus()) {
                subject.setStatus(false);
                subjectRepository.save(subject);
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_ACCEPTED, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.SUBJECT_DELETED, subject);
            } else {
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_NOT_FOUND, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.DATA_NOT_FOUND, subject);
            }
        } catch (Exception e) {
            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse deleteByName(String subjectName) {
        try {
            Subject subject = subjectRepository.findBySubjectName(subjectName);
            if (subject.getStatus()) {
                subject.setStatus(false);
                subjectRepository.save(subject);
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_ACCEPTED, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.SUBJECT_DELETED, subject);
            } else {
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_NOT_FOUND, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.DATA_NOT_FOUND, subject);
            }
        } catch (Exception e) {
            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    private void updateEntity(Subject subject, SubjectRequest request) {
        subject.setSubjectName(request.getSubjectName());
        subject.setDescription(request.getDescription());
        subject.setCredits(request.getCredits());
    }

    private SubjectResponse mapToResponse(Subject subject) {
        return new SubjectResponse(
                subject.getId(),
                subject.getSubjectName(),
                subject.getDescription(),
                subject.getCredits(),
                subject.getStatus()
        );
    }

    private Subject mapToEntity(SubjectRequest request) {
        return new Subject(
                null,
                request.getSubjectName(),
                request.getDescription(),
                request.getCredits(),
                true
        );
    }
}
