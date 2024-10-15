package com.school.SchoolManagement.RestController;

import com.school.SchoolManagement.Dto.Request.AdminRequest;
import com.school.SchoolManagement.Dto.Response.AdminResponse;
import com.school.SchoolManagement.Implementation.AdminImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sm/admin")
public class AdminController {
    @Autowired
    private AdminImpl adminImpl; n

    @PostMapping("/get")
    public ResponseEntity<List<AdminResponse>> getAll(){
        List<AdminResponse> admins = adminImpl.findAllAdmin();
        return ResponseEntity.ok(admins);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        try{
            AdminResponse admin = adminImpl.findById(id);
            return ResponseEntity.ok(admin);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
        }
    }

    @PostMapping("/set")
    public ResponseEntity<AdminResponse> create(@RequestBody AdminRequest adminRequest){
        AdminResponse adminResponse = adminImpl.createAdmin(adminRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(adminResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,@RequestBody AdminRequest adminRequest){
        try{
            AdminResponse existingAdmin = adminImpl.findById(id);
            if(existingAdmin != null){
                adminImpl.updateAdmin(id, adminRequest);
                return ResponseEntity.ok("Admin Updated Successfully!");
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not Update");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try{
            AdminResponse existingAdmin = adminImpl.findById(id);
            if(existingAdmin == null){
                return ResponseEntity.notFound().build();
            }
            adminImpl.deleteAdmin(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Admin Deleted Successfully!");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
