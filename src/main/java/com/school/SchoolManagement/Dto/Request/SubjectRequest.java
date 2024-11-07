package com.school.SchoolManagement.Dto.Request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectRequest {
    private Long id;

    @Column(name = "subject_name")
    @NotNull(message = "SubjectName cannot be empty")
    private String subjectName;

    @Column(name = "standard")
    @NotNull(message = "Standard cannot be empty")
    private Integer standard;

    @Column(name = "description")
    @NotNull(message = "Description cannot be empty")
    private String description;

    @Column(name = "credits")
    @NotNull(message = "Credits cannot be empty")
    private Long credits;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "stream")
    @NotNull(message = "Stream cannot be empty")
    private Long streamId;
}
