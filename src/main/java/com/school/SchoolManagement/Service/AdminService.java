package com.school.SchoolManagement.Service;

import ch.qos.logback.core.util.StringUtil;
import com.school.SchoolManagement.Dto.Request.AdminRequest;
import com.school.SchoolManagement.Dto.Response.AdminResponse;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Entity.Admin;
import com.school.SchoolManagement.Entity.User;
import com.school.SchoolManagement.Implementation.AdminImpl;
import com.school.SchoolManagement.Repository.AdminRepository;
import com.school.SchoolManagement.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.school.SchoolManagement.Constrants.RestMappingConstraints.*;

@Service
public class AdminService implements AdminImpl {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private UserRepository userRepository;

    public BaseApiResponse findAllAdmin() {
        try {
            List<Admin> admins = adminRepository.findAll();

            List<AdminResponse> activeAdmins = admins.stream()
                    .filter(admin -> admin.getStatus() != null && admin.getStatus())
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.ADMIN_FETCHED, activeAdmins);

        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    public BaseApiResponse findById(Long id) {
        try{
            if (id > 0) {
                Admin admin = adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not Found"));
                if (admin.getStatus()) {
                    return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.ADMIN_FETCHED, admin);
                }
                else {
                    return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
                }
            } else {
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_ACCEPTABLE, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.ID_NOT_ACCEPTABLE, Collections.emptyList());
            }

        } catch (Exception e) {
            if(e.getMessage().equals("Admin not Found")){
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
            }
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    public BaseApiResponse findByEmail(String email) {
        try{
            Admin admin = adminRepository.findByEmail(email);
            if (admin != null && admin.getStatus()) {
                return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.ADMIN_FETCHED, admin);
            }

            return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    public BaseApiResponse findByAdminName(String username) {
        try{
            List<Admin> admins = adminRepository.findByAdminName(username);
            List<AdminResponse> adminList = admins.stream()
                    .filter(admin -> admin.getStatus() != null && admin.getStatus())
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.ADMIN_FETCHED, adminList);
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }

    }

    public BaseApiResponse createOrUpdateAdmin(AdminRequest adminRequest) {
        try{
            if (adminRequest.getId() == null || adminRequest.getId() >= 0) {
                Admin admin = mapToEntity(adminRequest);

//                if (adminRequest.getId() == null || adminRequest.getId() == 0) {
////                    adminRepository.save(admin);
////                    return new BaseApiResponse(STATUS_CODES.HTTP_CREATED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.ADMIN_CREATED, admin);
//                } else {
                    Optional<Admin> existingAdminOpt = adminRepository.findById(adminRequest.getId());
                    if (existingAdminOpt.isPresent()) {
                        Admin existingAdmin = existingAdminOpt.get();
                        updateEntity(existingAdmin, adminRequest);
                        adminRepository.save(existingAdmin);
                        return new BaseApiResponse(STATUS_CODES.HTTP_ACCEPTED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.ADMIN_UPDATED, existingAdmin);
                    } else {
                        adminRepository.save(admin);
                        return new BaseApiResponse(STATUS_CODES.HTTP_CREATED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.ADMIN_CREATED, admin);
//                        return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
                    }
//                }
//            } else {
//                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_ACCEPTABLE, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.REQUEST_NOT_ACCEPTABLE, Collections.emptyList());
            }
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
        return null;
    }

    public BaseApiResponse createMultiple(List<AdminRequest> adminRequests){
        List<Admin> admin = new ArrayList<>();
        for (AdminRequest request: adminRequests){
            if (request.getId() == null || request.getId() == 0){
                Admin admin1 = mapToEntity(request);
                admin.add(admin1);
            }
            else{
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_ACCEPTABLE, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.ID_NOT_ACCEPTABLE, Collections.emptyList());
            }
        }
        adminRepository.saveAll(admin);
        return new BaseApiResponse(STATUS_CODES.HTTP_CREATED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.ADMIN_CREATED, admin);
    }

    public BaseApiResponse deleteAdmin(Long id) {
        try{
            Admin admin = adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not Found"));
            if(admin.getStatus()){
                admin.setStatus(false);
                adminRepository.save(admin);
                return new BaseApiResponse(STATUS_CODES.HTTP_ACCEPTED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.ADMIN_DELETED, admin);
            }
            else{
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.DATA_NOT_FOUND, admin);
            }

        }
        catch (Exception e){
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    public BaseApiResponse deleteAdminByEmail(String email) {
        try{
            Admin admin = adminRepository.findByEmail(email);
            if(admin.getStatus()){
                admin.setStatus(false);
                adminRepository.save(admin);
                return new BaseApiResponse(STATUS_CODES.HTTP_ACCEPTED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.ADMIN_DELETED, admin);
            }
            else{
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.DATA_NOT_FOUND, admin);
            }
        }
        catch (Exception e){
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    private void updateEntity(Admin admin, AdminRequest request) {
        admin.setAdminName(request.getAdminName());
        admin.setGender(request.getGender());
        admin.setAddress(request.getAddress());
        admin.setPhone(request.getPhone());
        admin.setDob(request.getDob());
//        admin.setPassword(request.getPassword());
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
                request.getEmail()
        );
    }

}
