package com.school.SchoolManagement.RestController;

import ch.qos.logback.core.util.StringUtil;
import com.school.SchoolManagement.Dto.Request.SearchRequest;
import com.school.SchoolManagement.Dto.Request.UserRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Implementation.UserImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/sm/user")
public class UserController {
    @Autowired
    private UserImpl userImpl;

    @PostMapping("/get")
    public ResponseEntity<BaseApiResponse> getAll(){
        BaseApiResponse baseApiResponse = userImpl.findAllUser();
        if(baseApiResponse.getSuccess() ==1){
            return ResponseEntity.ok(baseApiResponse);
        }
        else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseApiResponse);
        }
    }

    @PostMapping("/getbyid")
    public ResponseEntity<BaseApiResponse> getById(@RequestBody SearchRequest searchRequest){
        try{
            BaseApiResponse baseApiResponse = new BaseApiResponse("404", 0, "User not found", Collections.emptyList());
            if (searchRequest.getId() != null && searchRequest.getId() != 0){
                baseApiResponse = userImpl.findById(searchRequest.getId());
            }
            else if (searchRequest.getEmail() != null && !StringUtil.isNullOrEmpty(searchRequest.getEmail())) {
                baseApiResponse = userImpl.findByEmail(searchRequest.getEmail());
            }
//            else if (!StringUtil.isNullOrEmpty(searchRequest.getName())){
//                baseApiResponse = userImpl.findByUserName(searchRequest.getName());
//            }

            if (baseApiResponse.getSuccess() == 1) {
                return ResponseEntity.ok(baseApiResponse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseApiResponse);
            }
        }
        catch (RuntimeException e){
            if (e.getMessage().equals("User not Found")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse("404", 1, "User not found", Collections.emptyList()));
            }
            BaseApiResponse errorResponse = new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @PostMapping("/create")
    public ResponseEntity<BaseApiResponse> createOrUpdate(@RequestBody UserRequest request) {
        try {
            BaseApiResponse baseApiResponse = userImpl.createOrUpdateUser(request);
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

    @PostMapping("/delete")
    public ResponseEntity<BaseApiResponse> delete(@RequestBody SearchRequest searchRequest){
        try{
            BaseApiResponse baseApiResponse = new BaseApiResponse("404", 1, "User not found", Collections.emptyList());
            if(searchRequest.getId() != null && searchRequest.getId() != 0){
                baseApiResponse = userImpl.deleteUser(searchRequest.getId());
            }
            else if(!StringUtil.isNullOrEmpty(searchRequest.getEmail())){
                baseApiResponse = userImpl.deleteUserByEmail(searchRequest.getEmail());
            }

            if(baseApiResponse.getSuccess() == 1){
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(baseApiResponse);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse("404", 1, "User not found", Collections.emptyList()));
            }

        }catch (RuntimeException e){
            if (e.getMessage().equals("User not Found")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse("404", 1, "User not found", Collections.emptyList()));
            }
            BaseApiResponse errorResponse = new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
