package com.school.SchoolManagement.Service;

import com.school.SchoolManagement.Dto.Request.StudentRequest;
import com.school.SchoolManagement.Dto.Response.StudentResponse;
import com.school.SchoolManagement.Entity.Student;
import com.school.SchoolManagement.Implementation.StudentImpl;
import com.school.SchoolManagement.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService implements StudentImpl {

    @Autowired
    private StudentRepository studentRepository;

    public List<StudentResponse> findAllStudent() {
        try {
            List<Student> students = studentRepository.findAll();
            return students.stream()
                    .filter(student -> student.getStatus() != null && student.getStatus())
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Internal error occurred while fetching students");
        }
    }

    public StudentResponse findById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
        if (student.getStatus()) {
            return mapToResponse(student);
        }
        throw new RuntimeException("Student is inactive!");
    }

    public StudentResponse createStudent(StudentRequest studentRequest) {
        Student student = mapToEntity(studentRequest);
        studentRepository.save(student);
        return mapToResponse(student);
    }

    public void updateStudent(Long id, StudentRequest studentRequest) {
        Student existingStudent = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
        if (existingStudent.getStatus()) {
            updateEntity(existingStudent, studentRequest);
            studentRepository.save(existingStudent);
        } else {
            throw new RuntimeException("Student is inactive and cannot be updated!");
        }
    }

    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
        student.setStatus(false);
        studentRepository.save(student);
    }

    private Student mapToEntity(StudentRequest request) {
        return new Student(
                null,
                request.getFirstName(),
                request.getLastName(),
                request.getDob(),
                request.getEnrollmentDate(),
                request.getGender(),
                request.getAddress(),
                request.getPhone(),
                request.getGradeLevel(),
                true,
                request.getUsername(),
                request.getEmail(),
                request.getPassword()
        );
    }

    private void updateEntity(Student student, StudentRequest request) {
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setDob(request.getDob());
        student.setEnrollmentDate(request.getEnrollmentDate());
        student.setGender(request.getGender());
        student.setAddress(request.getAddress());
        student.setPhone(request.getPhone());
        student.setGradeLevel(request.getGradeLevel());
        student.setPassword(request.getPassword());
    }

    private StudentResponse mapToResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getDob(),
                student.getEnrollmentDate(),
                student.getGender(),
                student.getAddress(),
                student.getPhone(),
                student.getGradeLevel(),
                student.getUsername(),
                student.getEmail()
        );
    }
}

