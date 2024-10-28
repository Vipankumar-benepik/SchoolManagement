package com.school.SchoolManagement.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StreamResponse {
    private Long id;

    private String streamName;
    private Boolean status;

    // Stream Head is Teacher ID
    private Long streamHeadId;
}
