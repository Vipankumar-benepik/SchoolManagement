package com.school.SchoolManagement.Service;

import com.school.SchoolManagement.Dto.Request.UserRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Dto.Response.UserResponse;
import com.school.SchoolManagement.Entity.Admin;
import com.school.SchoolManagement.Entity.Role;
import com.school.SchoolManagement.Entity.Student;
import com.school.SchoolManagement.Entity.User;
import com.school.SchoolManagement.Implementation.UserImpl;
import com.school.SchoolManagement.Repository.AdminRepository;
import com.school.SchoolManagement.Repository.StudentRepository;
import com.school.SchoolManagement.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserImpl {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AdminRepository adminRepository;

    public BaseApiResponse findAllUser() {
        try {
            List<User> users = userRepository.findAll();

            List<UserResponse> activeUsers = users.stream()
                    .filter(user -> user.getStatus() != null && user.getStatus())
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            return new BaseApiResponse("200", 1, "Fetch Successful", activeUsers);

        } catch (Exception e) {
            return new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
        }
    }

    public BaseApiResponse findById(Long id) {
        try{
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not Found"));
            if (user.getStatus()) {
                return new BaseApiResponse("200", 1, "Fetch Successful ID", user);
            }
            else {
                return new BaseApiResponse("404", 1, "User not Found", Collections.emptyList());
            }
        } catch (Exception e) {
            if(e.getMessage().equals("User not Found")){
                return new BaseApiResponse("404", 1, "User not Found", Collections.emptyList());
            }
            return new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
        }
    }

    public BaseApiResponse findByEmail(String email) {
        try{
            User user = userRepository.findByEmail(email);
            if (user != null && user.getStatus()) {
                return new BaseApiResponse("200", 1, "Fetch Successful", user);
            }

            return new BaseApiResponse("404", 1, "User not Found", Collections.emptyList());
        } catch (Exception e) {
            return new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
        }
    }

//    public BaseApiResponse findByUserName(String username) {
//        try{
//            List<User> users = userRepository.findByUserName(username);
//
//            List<UserResponse> userList = users.stream()
//                    .filter(user -> user.getStatus() != null && user.getStatus())
//                    .map(this::mapToResponse)
//                    .collect(Collectors.toList());
//            return new BaseApiResponse("200", 1, "Fetch Successful", userList);
//        } catch (Exception e) {
//            return new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
//        }
//    }

    public BaseApiResponse createOrUpdateUser(UserRequest userRequest) {
        try{
            User user = mapToEntity(userRequest);

            if (userRequest.getId() == null || userRequest.getId() == 0) {
                if(userRequest.getRole().equals(Role.ADMIN)){
                    Admin existingAdmin = adminRepository.findById(userRequest.getRefId()).orElseThrow(() -> new RuntimeException("Admin not Found"));
                    if(existingAdmin != null && existingAdmin.getEmail().equals(userRequest.getEmail())){
                        user.setEmail(existingAdmin.getEmail());
                        userRepository.save(user);
                        return new BaseApiResponse("201", 1, "Created", user);
                    }
                    else{
                        if(!existingAdmin.getEmail().equals(userRequest.getEmail())){
                            return new BaseApiResponse("404", 0, "Admin Email "+ userRequest.getEmail() +" not available", Collections.emptyList());
                        }
                        return new BaseApiResponse("404", 0, "Admin Id "+ userRequest.getId() +" not available", user);
                    }
                }

                else if(userRequest.getRole().equals(Role.STUDENT)){
                    Student existingStudent = studentRepository.findById(userRequest.getRefId()).orElseThrow(() -> new RuntimeException("Student not Found"));
                    if(existingStudent != null && existingStudent.getEmail().equals(userRequest.getEmail())){
                        user.setEmail(existingStudent.getEmail());
                        userRepository.save(user);
                        return new BaseApiResponse("201", 1, "Created", user);
                    }
                    else{
                        if(!existingStudent.getEmail().equals(userRequest.getEmail())){
                            return new BaseApiResponse("404", 0, "Student Email "+ userRequest.getEmail() +" not available", Collections.emptyList());
                        }
                        return new BaseApiResponse("404", 0, "Student Id "+ userRequest.getId() +" not available", user);
                    }
                }

            } else {
                Optional<User> existingUserOpt = userRepository.findById(userRequest.getId());
                if (existingUserOpt.isPresent()) {
                    User existingUser = existingUserOpt.get();
                    updateEntity(existingUser, userRequest);
                    userRepository.save(existingUser);
                    return new BaseApiResponse("202", 1, "Updated", existingUser);
                } else {
                    return new BaseApiResponse("404", 0, "User not found", Collections.emptyList());
                }
            }

            return new BaseApiResponse("406", 0, "Not Acceptable", Collections.emptyList());

        } catch (Exception e) {
            if(e.getMessage().equals("Student not Found")){
                return new BaseApiResponse("404", 1, "Student not Found", Collections.emptyList());
            }else if(e.getMessage().equals("Admin not Found")){
                return new BaseApiResponse("404", 1, "Admin not Found", Collections.emptyList());
            }
            return new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
        }
    }

    public BaseApiResponse deleteUser(Long id) {
        try{
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not Found"));
            if(user.getStatus()){
                user.setStatus(false);
                userRepository.save(user);
                return new BaseApiResponse("202", 1, "Deleted Successfully", user);
            }
            else{
                return new BaseApiResponse("404", 0, "User not Found", user);
            }

        }
        catch (Exception e){
            return new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
        }
    }

    public BaseApiResponse deleteUserByEmail(String email) {
        try{
            User user = userRepository.findByEmail(email);
            if(user.getStatus()){
                user.setStatus(false);
                userRepository.save(user);
                return new BaseApiResponse("202", 0, "Deleted Successfully", user);
            }
            else{
                return new BaseApiResponse("404", 0, "User not Found", user);
            }

        }
        catch (Exception e){
            return new BaseApiResponse("500", 0, "Something went wrong", Collections.emptyList());
        }
    }

    private void updateEntity(User user, UserRequest request) {
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        user.setRefId(request.getRefId());
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getRefId()
        );
    }

    private User mapToEntity(UserRequest request) {
        return new User(
                null,
                null,
                request.getPassword(),
                true,
                request.getRole(),
                request.getRefId()
        );
    }
}
