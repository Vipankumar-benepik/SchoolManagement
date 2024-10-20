package com.school.SchoolManagement.Service;

import com.school.SchoolManagement.Dto.Request.StudentRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Dto.Response.StudentResponse;
import com.school.SchoolManagement.Entity.Student;
import com.school.SchoolManagement.Implementation.StudentImpl;
import com.school.SchoolManagement.Repository.StudentRepository;
import com.school.SchoolManagement.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService implements StudentImpl {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;

    public BaseApiResponse findAllStudent() {
        try {
            List<Student> students = studentRepository.findAll();

            List<StudentResponse> activeStudents = students.stream()
                    .filter(student -> student.getStatus() != null && student.getStatus())
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            return new BaseApiResponse("200", 1, "Fetch Successful", activeStudents);

        } catch (Exception e) {
            return new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
        }
    }

    public BaseApiResponse findById(Long id) {
        try{
            Student student = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not Found"));
            if (student.getStatus()) {
                return new BaseApiResponse("200", 1, "Fetch Successful", student);
            }
            else {
                return new BaseApiResponse("404", 1, "Student not Found", Collections.emptyList());
            }
        } catch (Exception e) {
            if(e.getMessage().equals("Student not Found")){
                return new BaseApiResponse("404", 1, "Not Found ID", Collections.emptyList());
            }
            return new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
        }
    }

    public BaseApiResponse findByEmail(String email) {
        try{
            Student student = studentRepository.findByEmail(email);
            if (student != null && student.getStatus()) {
                return new BaseApiResponse("200", 1, "Fetch Successful", student);
            }

            return new BaseApiResponse("404", 1, "Student not Found", Collections.emptyList());
        } catch (Exception e) {
            return new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
        }
    }

    public BaseApiResponse findByStudentName(String username) {
        try{
            List<Student> students = studentRepository.findByStudentName(username);
            List<StudentResponse> studentsList = students.stream()
                    .filter(student -> student.getStatus() != null && student.getStatus())
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            return new BaseApiResponse("200", 1, "Fetch Successful", studentsList);
        } catch (Exception e) {
            return new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
        }

    }

    public BaseApiResponse createOrUpdateStudent(StudentRequest studentRequest) {
        try{
            Student student = mapToEntity(studentRequest);

            if (studentRequest.getId() == null || studentRequest.getId() == 0) {
                studentRepository.save(student);
                return new BaseApiResponse("201", 1, "Created", student);
            } else {
                Optional<Student> existingStudentOpt = studentRepository.findById(studentRequest.getId());
                if (existingStudentOpt.isPresent()) {
                    Student existingStudent = existingStudentOpt.get();
                    updateEntity(existingStudent, studentRequest);
                    studentRepository.save(existingStudent);
                    return new BaseApiResponse("202", 1, "Updated", existingStudent);
                } else {
                    return new BaseApiResponse("404", 0, "Student not found", Collections.emptyList());
                }
            }
        } catch (Exception e) {
            return new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
        }

    }

    public BaseApiResponse createMultiple(List<StudentRequest> studentRequests){
        List<Student> students = new ArrayList<>();
        for (StudentRequest request: studentRequests){
            if (request.getId() == null || request.getId() == 0){
                Student student1 = mapToEntity(request);
                students.add(student1);
            }
            else{
                return new BaseApiResponse("406", 0, "Id's Not Acceptable", Collections.emptyList());
            }
        }
        studentRepository.saveAll(students);
        return new BaseApiResponse("201", 1, "All Created Successfully", students);
    }

    public BaseApiResponse deleteStudent(Long id) {
        try{
            Student student = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not Found"));
            if(student.getStatus()){
                student.setStatus(false);
                studentRepository.save(student);
                return new BaseApiResponse("202", 1, "Deleted Successfully", student);
            }
            else{
                return new BaseApiResponse("404", 0, "Student not Found", student);
            }

        }
        catch (Exception e){
            return new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
        }
    }

    public BaseApiResponse deleteStudentByEmail(String email) {
        try{
            Student student = studentRepository.findByEmail(email);
            if(student.getStatus()){
                student.setStatus(false);
                studentRepository.save(student);
                return new BaseApiResponse("202", 0, "Deleted Successfully", student);
            }
            else{
                return new BaseApiResponse("404", 0, "Student not Found", student);
            }

        }
        catch (Exception e){
            return new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
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

