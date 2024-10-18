package com.school.SchoolManagement.Dto.Request;

import lombok.Data;

@Data
public class SearchRequest {
    private Long id;
    private String email;
    private String name;
}
