package com.school.SchoolManagement.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseApiResponse {
    private String status;
    private int success;
    private String message;
    private Object data;
}
