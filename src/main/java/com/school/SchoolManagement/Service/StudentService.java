package com.school.SchoolManagement.Service;

import com.school.SchoolManagement.Dto.Request.StudentRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Dto.Response.StudentResponse;
import com.school.SchoolManagement.Entity.Student;
import com.school.SchoolManagement.Implementation.StudentImpl;
import com.school.SchoolManagement.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.school.SchoolManagement.Constrants.RestMappingConstraints.*;

@Service
public class StudentService implements StudentImpl {

    @Autowired
    private StudentRepository studentRepository;

    public BaseApiResponse findAllStudent() {
        try {
            List<Student> students = studentRepository.findAll();

            List<StudentResponse> activeStudents = students.stream()
                    .filter(student -> student.getStatus() != null && student.getStatus())
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.STUDENT_FETCHED, activeStudents);

        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    public BaseApiResponse findById(Long id) {
        try{
            Student student = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not Found"));
            if (student.getStatus()) {
                return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.STUDENT_FETCHED, student);
            }
            else {
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
            }
        } catch (Exception e) {
            if(e.getMessage().equals("Student not Found")){
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
            }
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    public BaseApiResponse findByEmail(String email) {
        try{
            Student student = studentRepository.findByEmail(email);
            if (student != null && student.getStatus()) {
                return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.STUDENT_FETCHED, student);
            }

            return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    public BaseApiResponse findByStudentName(String username) {
        try{
            List<Student> students = studentRepository.findByStudentName(username);
            List<StudentResponse> studentsList = students.stream()
                    .filter(student -> student.getStatus() != null && student.getStatus())
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.STUDENT_FETCHED, studentsList);
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }

    }

    public BaseApiResponse createOrUpdateStudent(StudentRequest studentRequest) {
        try{
            Student student = mapToEntity(studentRequest);

            if (studentRequest.getId() == null || studentRequest.getId() == 0) {
                studentRepository.save(student);
                return new BaseApiResponse(STATUS_CODES.HTTP_CREATED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.STUDENT_CREATED, student);
            } else {
                Optional<Student> existingStudentOpt = studentRepository.findById(studentRequest.getId());
                if (existingStudentOpt.isPresent()) {
                    Student existingStudent = existingStudentOpt.get();
                    updateEntity(existingStudent, studentRequest);
                    studentRepository.save(existingStudent);
                    return new BaseApiResponse(STATUS_CODES.HTTP_ACCEPTED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.STUDENT_UPDATED, existingStudent);
                } else {
                    return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
                }
            }
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    public BaseApiResponse createMultiple(List<StudentRequest> studentRequests){
        try{
            List<Student> students = new ArrayList<>();
            for (StudentRequest request: studentRequests){
                if (request.getId() == null || request.getId() == 0){
                    Student student1 = mapToEntity(request);
                    students.add(student1);
                }
                else{
                    return new BaseApiResponse(STATUS_CODES.HTTP_NOT_ACCEPTABLE, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.ID_NOT_ACCEPTABLE, Collections.emptyList());
                }
            }
            studentRepository.saveAll(students);
            return new BaseApiResponse(STATUS_CODES.HTTP_CREATED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.STUDENT_CREATED, students);
        }catch (Exception e){
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    public BaseApiResponse deleteStudent(Long id) {
        try{
            Student student = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not Found"));
            if(student.getStatus()){
                student.setStatus(false);
                studentRepository.save(student);
                return new BaseApiResponse(STATUS_CODES.HTTP_ACCEPTED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.STUDENT_DELETED, student);
            }
            else{
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.DATA_NOT_FOUND, student);
            }

        }
        catch (Exception e){
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    public BaseApiResponse deleteStudentByEmail(String email) {
        try{
            Student student = studentRepository.findByEmail(email);
            if(student.getStatus()){
                student.setStatus(false);
                studentRepository.save(student);
                return new BaseApiResponse(STATUS_CODES.HTTP_ACCEPTED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.STUDENT_DELETED, student);
            }
            else{
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.DATA_NOT_FOUND, student);
            }

        }
        catch (Exception e){
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    private void updateEntity(Student student, StudentRequest request) {
        student.setStudentName(request.getStudentName());
        student.setDob(request.getDob());
        student.setGender(request.getGender());
        student.setAddress(request.getAddress());
        student.setPhone(request.getPhone());
        student.setGradeLevel(request.getGradeLevel());
//        student.setPassword(request.getPassword());
    }

    private StudentResponse mapToResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getStudentName(),
                student.getDob(),
                student.getEnrollmentDate(),
                student.getGender(),
                student.getAddress(),
                student.getPhone(),
                student.getGradeLevel(),
                student.getStatus(),
                student.getEmail()
        );
    }

    private Student mapToEntity(StudentRequest request) {
        return new Student(
                null,
                request.getStudentName(),
                request.getDob(),
                request.getEnrollmentDate(),
                request.getGender(),
                request.getAddress(),
                request.getPhone(),
                request.getGradeLevel(),
                true,
                request.getEmail()
        );
    }
}

