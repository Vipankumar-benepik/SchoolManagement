package com.school.SchoolManagement.Service;

import com.school.SchoolManagement.Constrants.RestMappingConstraints.*;
import com.school.SchoolManagement.Dto.Request.SubjectRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Dto.Response.StudentResponse;
import com.school.SchoolManagement.Dto.Response.SubjectResponse;
import com.school.SchoolManagement.Entity.Standard;
import com.school.SchoolManagement.Entity.Stream;
import com.school.SchoolManagement.Entity.Student;
import com.school.SchoolManagement.Entity.Subject;
import com.school.SchoolManagement.Implementation.SubjectImpl;
import com.school.SchoolManagement.Repository.StandardRepository;
import com.school.SchoolManagement.Repository.StreamRepository;
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

    @Autowired
    private StandardRepository standardRepository;

    @Autowired
    private StreamRepository streamRepository;

    @Override
    public BaseApiResponse findAll() {
        try {
            List<Subject> subjects = subjectRepository.findAll();

            List<SubjectResponse> activeSubjects = subjects.stream()
                    .filter(subject -> subject.getStatus() != null && subject.getStatus())
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.SUBJECT_FETCHED, activeSubjects);
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse findById(Long id) {
        try {
            Subject subject = subjectRepository.findById(id).orElseThrow(() -> new RuntimeException("Subject not Found"));
            if (subject.getStatus()) {
                return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.SUBJECT_FETCHED, subject);
            } else {
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
            }
        } catch (Exception e) {
            if (e.getMessage().equals("Subject not Found")) {
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
            }
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse findByName(String subjectName) {
        try {
            Subject subject = subjectRepository.findBySubjectName(subjectName);
            if (subject != null && subject.getStatus()) {
                return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.SUBJECT_FETCHED, subject);
            }
            return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    public BaseApiResponse findByStandard(Integer standardId) {
        try {
            List<Subject> subjects = subjectRepository.findByStandard(standardId);
            List<SubjectResponse> subjectsList = subjects.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.SUBJECT_FETCHED, subjectsList);
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());

        }
    }

    @Override
    public BaseApiResponse createOrUpdate(SubjectRequest subjectRequest) {
        try {
            if (subjectRequest.getId() == null || subjectRequest.getId() >= 0) {
                Subject subject = mapToEntity(subjectRequest);
                Optional<Standard> standard = standardRepository.findById(Long.valueOf(subjectRequest.getStandard()));
                Optional<Stream> stream = streamRepository.findById(subjectRequest.getStreamId());

                if (subjectRequest.getId() == null || subjectRequest.getId() == 0) {
                    if (stream.isPresent() && standard.isPresent()){
                        subjectRepository.save(subject);
                        return new BaseApiResponse(STATUS_CODES.HTTP_CREATED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.SUBJECT_CREATED, subject);
                    } else {
                        return new BaseApiResponse(STATUS_CODES.HTTP_NOT_ACCEPTABLE, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.REQUEST_NOT_ACCEPTABLE, Collections.emptyList());
                    }
                } else {
                    Optional<Subject> existingSubjectOpt = subjectRepository.findById(subject.getId());
                    if (existingSubjectOpt.isPresent()) {
                        Subject existingSubject = existingSubjectOpt.get();
                        updateEntity(existingSubject, subjectRequest);
                        subjectRepository.save(existingSubject);
                        return new BaseApiResponse(STATUS_CODES.HTTP_ACCEPTED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.SUBJECT_UPDATED, existingSubject);
                    } else {
                        return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
                    }
                }
            } else {
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_ACCEPTABLE, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.REQUEST_NOT_ACCEPTABLE, Collections.emptyList());
            }

        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, e.getMessage(), Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse delete(Long id) {
        try {
            Subject subject = subjectRepository.findById(id).orElseThrow(() -> new RuntimeException("Subject not Found"));
            if (subject.getStatus()) {
                subject.setStatus(false);
                subjectRepository.save(subject);
                return new BaseApiResponse(STATUS_CODES.HTTP_ACCEPTED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.SUBJECT_DELETED, subject);
            } else {
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.DATA_NOT_FOUND, subject);
            }
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse deleteByName(String subjectName) {
        try {
            Subject subject = subjectRepository.findBySubjectName(subjectName);
            if (subject.getStatus()) {
                subject.setStatus(false);
                subjectRepository.save(subject);
                return new BaseApiResponse(STATUS_CODES.HTTP_ACCEPTED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.SUBJECT_DELETED, subject);
            } else {
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.DATA_NOT_FOUND, subject);
            }
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    private void updateEntity(Subject subject, SubjectRequest request) {
        subject.setSubjectName(request.getSubjectName());
        subject.setStandard(request.getStandard());
        subject.setDescription(request.getDescription());
        subject.setCredits(request.getCredits());
    }

    private SubjectResponse mapToResponse(Subject subject) {
        return new SubjectResponse(
                subject.getId(),
                subject.getSubjectName(),
                subject.getStandard(),
                subject.getDescription(),
                subject.getCredits(),
                subject.getStatus(),
                subject.getStreamId()
        );
    }

    private Subject mapToEntity(SubjectRequest request) {
        return new Subject(
                null,
                request.getSubjectName(),
                request.getStandard(),
                request.getDescription(),
                request.getCredits(),
                true,
                request.getStreamId()
        );
    }
}
