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
public class Stream {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "stream_name", unique = true)
    @NotNull(message = "stream Name cannot be empty")
    private String streamName;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "streamhead_id")
    @NotNull(message = "StreamHeadId cannot be empty")
    // Stream Head is Teacher ID
    private Long streamHeadId;
}
