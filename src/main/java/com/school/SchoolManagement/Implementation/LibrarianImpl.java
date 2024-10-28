package com.school.SchoolManagement.Implementation;

import com.school.SchoolManagement.Dto.Request.LibrarianRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;

public interface LibrarianImpl {
    BaseApiResponse findAll();
    BaseApiResponse findById(Long id);
    BaseApiResponse findByEmail(String email);
    BaseApiResponse findByName(String username);
    BaseApiResponse createOrUpdate(LibrarianRequest librarianRequest);
    BaseApiResponse delete(Long id);
    BaseApiResponse deleteByEmail(String email);
}
