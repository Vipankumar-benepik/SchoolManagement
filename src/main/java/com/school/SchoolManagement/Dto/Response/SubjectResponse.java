package com.school.SchoolManagement.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectResponse {
    private Long id;

    private String subjectName;
    private Integer standard;
    private String description;
    private Long credits;
    private Boolean status;
    private Long streamId;
}
