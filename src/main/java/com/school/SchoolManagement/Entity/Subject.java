package com.school.SchoolManagement.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
