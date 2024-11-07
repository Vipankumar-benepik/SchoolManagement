package com.school.SchoolManagement.Dto.Request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class StreamRequest {
    private Long id;

    @Column(name = "stream_name")
    @NotNull(message = "stream Name cannot be empty")
    private String streamName;

    @Column(name = "status")
    private Boolean status;

    // Stream Head is Teacher ID
    @Column(name = "streamhead_id")
    @NotNull(message = "StreamHeadId cannot be empty")
    private Long streamHeadId;
}
