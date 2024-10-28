package com.school.SchoolManagement.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectRequest {
    private Long id;

    private String subjectName;
    private String description;
    private Long credits;
    private Boolean status;
}
