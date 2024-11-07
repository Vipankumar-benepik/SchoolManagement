package com.school.SchoolManagement.Dto.Request.SearchRequestDto;

import lombok.Data;

@Data
public class SearchRequest {
    private Long id;
    private String email;
    private String name;
    private Long parentId;
}
