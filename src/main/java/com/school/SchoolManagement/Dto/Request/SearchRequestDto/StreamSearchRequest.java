package com.school.SchoolManagement.Dto.Request.SearchRequestDto;

import lombok.Data;

@Data
public class StreamSearchRequest {
    private Long id;
    private String name;
    private Long streamHeadId;
}
