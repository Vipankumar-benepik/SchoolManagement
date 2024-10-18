package com.school.SchoolManagement.Implementation;

import com.school.SchoolManagement.Dto.Request.UserRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;

import java.util.List;

public interface UserImpl {
    BaseApiResponse findAllUser();
    BaseApiResponse findById(Long id);
    BaseApiResponse findByEmail(String email);
//    BaseApiResponse findByUserName(String username);
    BaseApiResponse createOrUpdateUser(UserRequest userRequest);
    BaseApiResponse deleteUser(Long id);
    BaseApiResponse deleteUserByEmail(String username);
}
