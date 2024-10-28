package com.school.SchoolManagement.Implementation;

import com.school.SchoolManagement.Dto.Request.StreamRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;

public interface StreamImpl {
    BaseApiResponse findAll();
    BaseApiResponse findById(Long id);
    BaseApiResponse findByName(String streamName);
    BaseApiResponse findByStreamHeadId(Long streamHeadId);
    BaseApiResponse createOrUpdate(StreamRequest streamRequest);
    BaseApiResponse delete(Long id);
    BaseApiResponse deleteByName(String streamName);
}
