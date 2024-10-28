package com.school.SchoolManagement.Dto.Request.SearchRequestDto;

import lombok.Data;

@Data
public class LibrarySearchRequest {
    private Long id;
    private String title;
    private String author;
}
