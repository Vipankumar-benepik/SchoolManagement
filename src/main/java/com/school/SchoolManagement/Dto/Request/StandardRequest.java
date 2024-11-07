package com.school.SchoolManagement.Dto.Request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StandardRequest {
    private Long id;

    @Column(name = "standard_name", unique = true, length = 50)
    private String standardName;
}
