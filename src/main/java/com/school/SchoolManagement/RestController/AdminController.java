package com.school.SchoolManagement.RestController;

import ch.qos.logback.core.util.StringUtil;
import com.school.SchoolManagement.Dto.Request.AdminRequest;
import com.school.SchoolManagement.Dto.Request.SearchRequest;
import com.school.SchoolManagement.Dto.Response.AdminResponse;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Implementation.AdminImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/sm/admin")
public class AdminController {
    @Autowired
    private AdminImpl adminImpl;

    @PostMapping("/get")
    public ResponseEntity<BaseApiResponse> getAll(){
        try{
            BaseApiResponse admins = adminImpl.findAllAdmin();
            if(admins.getSuccess() ==1){
                return ResponseEntity.ok(admins);
            }
            else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(admins);
            }
        } catch (Exception e) {
            BaseApiResponse errorResponse = new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/getbyid")
    public ResponseEntity<BaseApiResponse> getById(@RequestBody SearchRequest searchRequest){
        try{
            BaseApiResponse baseApiResponse = new BaseApiResponse("404", 0, "Admin not found", Collections.emptyList());
            if (searchRequest.getId() != null && searchRequest.getId() != 0){
                baseApiResponse = adminImpl.findById(searchRequest.getId());
            }
            else if (searchRequest.getEmail() != null && !StringUtil.isNullOrEmpty(searchRequest.getEmail())) {
                baseApiResponse = adminImpl.findByEmail(searchRequest.getEmail());
            }
            else if (!StringUtil.isNullOrEmpty(searchRequest.getName())){
                baseApiResponse = adminImpl.findByAdminName(searchRequest.getName());
            }

            if (baseApiResponse.getSuccess() == 1) {
                return ResponseEntity.ok(baseApiResponse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseApiResponse);
            }
        }
        catch (RuntimeException e){
            if (e.getMessage().equals("Admin not Found")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse("404", 1, "Admin not found", Collections.emptyList()));
            }
            BaseApiResponse errorResponse = new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @PostMapping("/create")
    public ResponseEntity<BaseApiResponse> createOrUpdate(@RequestBody AdminRequest request) {
        try {
            BaseApiResponse baseApiResponse = adminImpl.createOrUpdateAdmin(request);
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
    public ResponseEntity<BaseApiResponse> createBatch(@RequestBody List<AdminRequest> requests){
        try{
            BaseApiResponse baseApiResponse = adminImpl.createMultiple(requests);
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
            BaseApiResponse baseApiResponse = new BaseApiResponse("404", 1, "Admin not found", Collections.emptyList());
            if(searchRequest.getId() != null && searchRequest.getId() != 0){
                baseApiResponse = adminImpl.deleteAdmin(searchRequest.getId());
            }
            else if(!StringUtil.isNullOrEmpty(searchRequest.getEmail())){
                baseApiResponse = adminImpl.deleteAdminByEmail(searchRequest.getEmail());
            }

            if(baseApiResponse.getSuccess() == 1){
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(baseApiResponse);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse("404", 1, "Admin not found", Collections.emptyList()));
            }

        }catch (RuntimeException e){
            if (e.getMessage().equals("Admin not Found")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse("404", 1, "Admin not found", Collections.emptyList()));
            }
            BaseApiResponse errorResponse = new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}
