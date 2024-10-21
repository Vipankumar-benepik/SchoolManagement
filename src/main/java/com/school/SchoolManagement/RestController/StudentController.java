package com.school.SchoolManagement.RestController;

import ch.qos.logback.core.util.StringUtil;
import com.school.SchoolManagement.Dto.Request.SearchRequest;
import com.school.SchoolManagement.Dto.Request.StudentRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Implementation.StudentImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/sm/student")
public class StudentController {
    @Autowired
    private StudentImpl studentImpl;

    @PostMapping("/get")
    public ResponseEntity<BaseApiResponse> getAll(){
        try{
            BaseApiResponse students = studentImpl.findAllStudent();
            if(students.getSuccess() ==1){
                return ResponseEntity.ok(students);
            }
            else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(students);
            }
        } catch (Exception e) {
            BaseApiResponse errorResponse = new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/getbyid")
    public ResponseEntity<BaseApiResponse> getById(@RequestBody SearchRequest searchRequest){
        try{
            BaseApiResponse baseApiResponse = new BaseApiResponse("404", 0, "Student not found", Collections.emptyList());
            if (searchRequest.getId() != null && searchRequest.getId() != 0){
                baseApiResponse = studentImpl.findById(searchRequest.getId());
            }
            else if (searchRequest.getEmail() != null && !StringUtil.isNullOrEmpty(searchRequest.getEmail())) {
                baseApiResponse = studentImpl.findByEmail(searchRequest.getEmail());
            }
            else if (!StringUtil.isNullOrEmpty(searchRequest.getName())){
                baseApiResponse = studentImpl.findByStudentName(searchRequest.getName());
            }

            if (baseApiResponse.getSuccess() == 1) {
                return ResponseEntity.ok(baseApiResponse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseApiResponse);
            }
        }
        catch (RuntimeException e){
            if (e.getMessage().equals("Student not Found")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse("404", 1, "Student not found", Collections.emptyList()));
            }
            BaseApiResponse errorResponse = new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @PostMapping("/create")
    public ResponseEntity<BaseApiResponse> createOrUpdate(@RequestBody StudentRequest request) {
        try {
            BaseApiResponse baseApiResponse = studentImpl.createOrUpdateStudent(request);
            if(baseApiResponse.getSuccess() == 1){
                if (request.getId() == null || request.getId() == 0) {
                    return ResponseEntity.status(HttpStatus.CREATED).body(baseApiResponse);
                } else {
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(baseApiResponse);
                }
            }
            else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseApiResponse);
            }
        } catch (RuntimeException e) {
            BaseApiResponse errorResponse = new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/batch")
    public ResponseEntity<BaseApiResponse> createBatch(@RequestBody List<StudentRequest> requests){
        try{
            BaseApiResponse baseApiResponse = studentImpl.createMultiple(requests);
            if(baseApiResponse.getSuccess() == 1){
                return ResponseEntity.status(HttpStatus.CREATED).body(baseApiResponse);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(baseApiResponse);
            }
        } catch (RuntimeException e) {
            BaseApiResponse errorResponse = new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<BaseApiResponse> delete(@RequestBody SearchRequest searchRequest){
        try{
            BaseApiResponse baseApiResponse = new BaseApiResponse("404", 1, "Student not found", Collections.emptyList());
            if(searchRequest.getId() != null && searchRequest.getId() != 0){
                baseApiResponse = studentImpl.deleteStudent(searchRequest.getId());
            }
            else if(!StringUtil.isNullOrEmpty(searchRequest.getEmail())){
                baseApiResponse = studentImpl.deleteStudentByEmail(searchRequest.getEmail());
            }

            if(baseApiResponse.getSuccess() == 1){
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(baseApiResponse);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse("404", 1, "Student not found", Collections.emptyList()));
            }

        }catch (RuntimeException e){
            if (e.getMessage().equals("Student not Found")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse("404", 1, "Student not found", Collections.emptyList()));
            }
            BaseApiResponse errorResponse = new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
