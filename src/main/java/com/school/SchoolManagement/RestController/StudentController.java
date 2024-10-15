package com.school.SchoolManagement.RestController;

import com.school.SchoolManagement.Dto.Request.StudentRequest;
import com.school.SchoolManagement.Dto.Response.StudentResponse;
import com.school.SchoolManagement.Implementation.StudentImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sm/student")
public class StudentController {
    @Autowired
    private StudentImpl studentImpl;

    @GetMapping("/get")
    public ResponseEntity<List<StudentResponse>> getAll(){
        List<StudentResponse> students = studentImpl.findAllStudent();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        try{
            StudentResponse student = studentImpl.findById(id);
            return ResponseEntity.ok(student);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
        }
    }

    @PostMapping("/set")
    public ResponseEntity<StudentResponse> create(@RequestBody StudentRequest studentRequest){
        StudentResponse studentResponse = studentImpl.createStudent(studentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(studentResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,@RequestBody StudentRequest studentRequest){
        try{
            StudentResponse existingStudent = studentImpl.findById(id);
            if(existingStudent != null){
                studentImpl.updateStudent(id, studentRequest);
                return ResponseEntity.ok("Student Updated Successfully!");
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not Update");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try{
            StudentResponse existingStudent = studentImpl.findById(id);
            if(existingStudent == null){
                return ResponseEntity.notFound().build();
            }
            studentImpl.deleteStudent(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Student Deleted Successfully!");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
