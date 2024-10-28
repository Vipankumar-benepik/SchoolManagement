package com.school.SchoolManagement.Service;

import com.school.SchoolManagement.Dto.Request.TeacherRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Dto.Response.TeacherResponse;
import com.school.SchoolManagement.Entity.Teacher;
import com.school.SchoolManagement.Implementation.TeacherImpl;
import com.school.SchoolManagement.Repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.school.SchoolManagement.Constrants.RestMappingConstraints.*;

@Service
public class TeacherService implements TeacherImpl {
    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public BaseApiResponse findAllTeacher() {
        try {
            List<Teacher> teachers = teacherRepository.findAll();

            List<TeacherResponse> activeTeachers = teachers.stream()
                    .filter(teacher -> teacher.getStatus() != null && teacher.getStatus())
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.TEACHER_FETCHED, activeTeachers);
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse findById(Long id) {
        try {
            Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new RuntimeException("Teacher not Found"));
            if (teacher.getStatus()) {
                return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.TEACHER_FETCHED, teacher);
            } else {
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
            }
        } catch (Exception e) {
            if (e.getMessage().equals("Teacher not Found")) {
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
            }
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse findByEmail(String email) {
        try {
            Teacher teacher = teacherRepository.findByEmail(email);
            if (teacher != null && teacher.getStatus()) {
                return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.TEACHER_FETCHED, teacher);
            }
            return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse findByTeacherName(String username) {
        try {
            List<Teacher> teachers = teacherRepository.findByTeacherName(username);
            List<TeacherResponse> teacherList = teachers.stream()
                    .filter(teacher -> teacher.getStatus() != null && teacher.getStatus())
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.TEACHER_FETCHED, teacherList);
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse createOrUpdateTeacher(TeacherRequest teacherRequest) {
        try {
            Teacher teacher = mapToEntity(teacherRequest);

            if (teacherRequest.getId() == null || teacherRequest.getId() == 0) {
                teacherRepository.save(teacher);
                return new BaseApiResponse(STATUS_CODES.HTTP_CREATED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.TEACHER_CREATED, teacher);
            } else {
                Optional<Teacher> existingTeacherOpt = teacherRepository.findById(teacher.getId());
                if (existingTeacherOpt.isPresent()) {
                    Teacher existingTeacher = existingTeacherOpt.get();
                    updateEntity(existingTeacher, teacherRequest);
                    teacherRepository.save(existingTeacher);
                    return new BaseApiResponse(STATUS_CODES.HTTP_ACCEPTED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.TEACHER_UPDATED, existingTeacher);
                } else {
                    return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
                }
            }
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse deleteTeacher(Long id) {
        try{
            Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new RuntimeException("Teacher not Found"));
            if(teacher.getStatus()){
                teacher.setStatus(false);
                teacherRepository.save(teacher);
                return new BaseApiResponse(STATUS_CODES.HTTP_ACCEPTED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.TEACHER_DELETED, teacher);
            }
            else{
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.DATA_NOT_FOUND, teacher);
            }
        }catch (Exception e){
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse deleteTeacherByEmail(String email) {
        try{
            Teacher teacher = teacherRepository.findByEmail(email);
            if(teacher.getStatus()){
                teacher.setStatus(false);
                teacherRepository.save(teacher);
                return new BaseApiResponse(STATUS_CODES.HTTP_ACCEPTED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.TEACHER_DELETED, teacher);
            }
            else{
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.DATA_NOT_FOUND, teacher);
            }
        }catch (Exception e){
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    private void updateEntity(Teacher teacher, TeacherRequest request) {
        teacher.setTeacherName(request.getTeacherName());
        teacher.setDob(request.getDob());
        teacher.setGender(request.getGender());
        teacher.setAddress(request.getAddress());
        teacher.setPhone(request.getPhone());
        teacher.setSalary(request.getSalary());
        teacher.setSpecialization(request.getSpecialization());
//        teacher.setPassword(request.getPassword());
    }

    private TeacherResponse mapToResponse(Teacher teacher) {
        return new TeacherResponse(
                teacher.getId(),
                teacher.getTeacherName(),
                teacher.getDob(),
                teacher.getGender(),
                teacher.getAddress(),
                teacher.getPhone(),
                teacher.getSalary(),
                teacher.getSpecialization(),
                teacher.getHireDate(),
                teacher.getStreamId(),
                teacher.getCourseId(),
                teacher.getEmail()
        );
    }

    private Teacher mapToEntity(TeacherRequest request) {
        return new Teacher(
                null,
                request.getTeacherName(),
                request.getDob(),
                request.getGender(),
                request.getAddress(),
                request.getPhone(),
                request.getSalary(),
                request.getSpecialization(),
                new Date(),
                true,
                request.getSteamId(),
                request.getCourseId(),
                request.getEmail()
        );
    }
}
