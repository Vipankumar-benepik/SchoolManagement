package com.school.SchoolManagement.Service;

import ch.qos.logback.core.util.StringUtil;
import com.school.SchoolManagement.Dto.Request.AdminRequest;
import com.school.SchoolManagement.Dto.Response.AdminResponse;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Entity.Admin;
import com.school.SchoolManagement.Implementation.AdminImpl;
import com.school.SchoolManagement.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService implements AdminImpl {

    @Autowired
    private AdminRepository adminRepository;

    public BaseApiResponse findAllAdmin() {
        try {
            List<Admin> admins = adminRepository.findAll();

            List<AdminResponse> activeAdmins = admins.stream()
                    .filter(admin -> admin.getStatus() != null && admin.getStatus())
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            return new BaseApiResponse("200", 1, "Fetch Successful", activeAdmins);

        } catch (Exception e) {
            return new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
        }
    }

    public BaseApiResponse findById(Long id) {
        try{
            Admin admin = adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not Found"));
            if (admin.getStatus()) {
                return new BaseApiResponse("200", 1, "Fetch Successful", admin);
            }
            else {
                return new BaseApiResponse("404", 1, "Not Found", Collections.emptyList());
            }
        } catch (Exception e) {
            if(e.getMessage().equals("Admin not Found")){
                return new BaseApiResponse("404", 1, "Not Found ID", Collections.emptyList());
            }
            return new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
        }
    }

    public BaseApiResponse findByEmail(String email) {
        try{
            Admin admin = adminRepository.findByEmail(email);
            if (admin != null && admin.getStatus()) {
                return new BaseApiResponse("200", 1, "Fetch Successful", admin);
            }

            return new BaseApiResponse("404", 1, "Not Found", Collections.emptyList());
        } catch (Exception e) {
            return new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
        }
    }

    public BaseApiResponse findByAdminName(String username) {
        try{
            List<Admin> admins = adminRepository.findByAdminName(username);
            List<AdminResponse> adminList = admins.stream()
                    .filter(admin -> admin.getStatus() != null && admin.getStatus())
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            return new BaseApiResponse("200", 1, "Fetch Successful", adminList);
        } catch (Exception e) {
            return new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
        }

    }

    public BaseApiResponse createOrUpdateAdmin(AdminRequest adminRequest) {
        try{
            Admin admin = mapToEntity(adminRequest);

            if (adminRequest.getId() == null || adminRequest.getId() == 0) {
                adminRepository.save(admin);
                return new BaseApiResponse("201", 1, "Created", admin);
            } else {
                Optional<Admin> existingAdminOpt = adminRepository.findById(adminRequest.getId());
                if (existingAdminOpt.isPresent()) {
                    Admin existingAdmin = existingAdminOpt.get();
                    updateEntity(existingAdmin, adminRequest);
                    adminRepository.save(existingAdmin);
                    return new BaseApiResponse("202", 1, "Updated", existingAdmin);
                } else {
                    return new BaseApiResponse("404", 0, "Admin not found", Collections.emptyList());
                }
            }
        } catch (Exception e) {
            return new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
        }

    }

    public BaseApiResponse createMultiple(List<AdminRequest> adminRequests){
        List<Admin> admin = new ArrayList<>();
        for (AdminRequest request: adminRequests){
            if (request.getId() == null || request.getId() == 0){
                Admin admin1 = mapToEntity(request);
                admin.add(admin1);
            }
            else{
                return new BaseApiResponse("406", 0, "Id's Not Acceptable", Collections.emptyList());
            }
        }
        adminRepository.saveAll(admin);
        return new BaseApiResponse("201", 1, "All Created Successfully", admin);
    }

    public BaseApiResponse deleteAdmin(Long id) {
        try{
            Admin admin = adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not Found"));
            if(admin.getStatus()){
                admin.setStatus(false);
                adminRepository.save(admin);
                return new BaseApiResponse("202", 1, "Deleted Successfully", admin);
            }
            else{
                return new BaseApiResponse("404", 0, "Not Found", admin);
            }

        }
        catch (Exception e){
            return new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
        }
    }

    public BaseApiResponse deleteAdminByEmail(String email) {
        try{
            Admin admin = adminRepository.findByEmail(email);
            if(admin.getStatus()){
                admin.setStatus(false);
                adminRepository.save(admin);
                return new BaseApiResponse("202", 0, "Deleted Successfully", admin);
            }
            else{
                return new BaseApiResponse("404", 0, "Not Found", admin);
            }

        }
        catch (Exception e){
            return new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
        }
    }

    private void updateEntity(Admin admin, AdminRequest request) {
        admin.setAdminName(request.getAdminName());
        admin.setGender(request.getGender());
        admin.setAddress(request.getAddress());
        admin.setPhone(request.getPhone());
        admin.setDob(request.getDob());
        admin.setPassword(request.getPassword());
    }

    private AdminResponse mapToResponse(Admin admin) {
        return new AdminResponse(
                admin.getId(),
                admin.getAdminName(),
                admin.getGender(),
                admin.getAddress(),
                admin.getPhone(),
                admin.getDob(),
                admin.getEmail()
        );
    }

    private Admin mapToEntity(AdminRequest request) {
        return new Admin(
                null,
                request.getAdminName(),
                request.getGender(),
                request.getAddress(),
                request.getPhone(),
                request.getDob(),
                true,
                request.getEmail(),
                request.getPassword()
        );
    }

}
