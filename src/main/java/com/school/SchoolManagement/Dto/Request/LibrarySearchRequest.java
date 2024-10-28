package com.school.SchoolManagement.Dto.Request;

import lombok.Data;

@Data
public class LibrarySearchRequest {
    private Long id;
    private String title;
    private String author;
}
