package com.school.SchoolManagement.Repository;

import com.school.SchoolManagement.Entity.Stream;
import com.school.SchoolManagement.Utils.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StreamRepository extends GenericRepository<Stream, Long> {
    Stream findByStreamName(String streamName);
    List<Stream> findByStreamHeadId(Long streamHeadId);
}
