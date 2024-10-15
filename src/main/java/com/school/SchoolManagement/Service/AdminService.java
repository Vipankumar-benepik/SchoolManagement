package com.school.SchoolManagement.Service;

import com.school.SchoolManagement.Dto.Request.AdminRequest;
import com.school.SchoolManagement.Dto.Response.AdminResponse;
import com.school.SchoolManagement.Entity.Admin;
import com.school.SchoolManagement.Implementation.AdminImpl;
import com.school.SchoolManagement.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService implements AdminImpl {

    @Autowired
    private AdminRepository adminRepository;

    public List<AdminResponse> findAllAdmin(){
        List<Admin> admins = adminRepository.findAll();
        return admins.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public AdminResponse findById(Long id){
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not Found!"));
        return mapToResponse(admin);
    }

    public AdminResponse createAdmin(AdminRequest adminRequest){
        Admin admin = mapToEntity(adminRequest);
        adminRepository.save(admin);
        return mapToResponse(admin);
    }

    public void updateAdmin(Long id, AdminRequest adminRequest){
        Admin existingadmin = adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not Found!"));
        updateEntity(existingadmin, adminRequest);
        mapToResponse(existingadmin);
    }

    public void deleteAdmin(Long id){
        adminRepository.deleteById(id);
    }

    private void updateEntity(Admin admin, AdminRequest request){
        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        admin.setGender(request.getGender());
        admin.setAddress(request.getAddress());
        admin.setPhone(request.getPhone());
        admin.setDob(request.getDob());
        admin.setPassword(request.getPassword());
    }

    private AdminResponse mapToResponse(Admin admin){
        return new AdminResponse(
                admin.getId(),
                admin.getFirstName(),
                admin.getLastName(),
                admin.getGender(),
                admin.getAddress(),
                admin.getPhone(),
                admin.getDob(),
                admin.getUsername(),
                admin.getEmail()
        );
    }

    private Admin mapToEntity(AdminRequest request){
        return new Admin(
                null,
                request.getFirstName(),
                request.getLastName(),
                request.getGender(),
                request.getAddress(),
                request.getPhone(),
                request.getDob(),
                request.getUsername(),
                request.getEmail(),
                request.getPassword()
        );
    }


}
