package com.school.SchoolManagement.Service;

import com.school.SchoolManagement.Constrants.RestMappingConstraints;
import com.school.SchoolManagement.Dto.Request.StreamRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Dto.Response.StreamResponse;
import com.school.SchoolManagement.Entity.Stream;
import com.school.SchoolManagement.Implementation.StreamImpl;
import com.school.SchoolManagement.Repository.StreamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StreamService implements StreamImpl {
    @Autowired
    private StreamRepository streamRepository;

    @Override
    public BaseApiResponse findAll() {
        try {
            List<Stream> streams = streamRepository.findAll();

            List<StreamResponse> activeStreams = streams.stream()
                    .filter(stream -> stream.getStatus() != null && stream.getStatus())
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_OK, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.STREAM_FETCHED, activeStreams);
        } catch (Exception e) {
            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse findById(Long id) {
        try {
            Stream stream = streamRepository.findById(id).orElseThrow(() -> new RuntimeException("Stream not Found"));
            if (stream.getStatus()) {
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_OK, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.STREAM_FETCHED, stream);
            } else {
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_NOT_FOUND, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
            }
        } catch (Exception e) {
            if (e.getMessage().equals("Stream not Found")) {
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_NOT_FOUND, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
            }
            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse findByName(String streamName) {
        try {
            Stream stream = streamRepository.findByStreamName(streamName);
            if (stream != null && stream.getStatus()) {
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_OK, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.STREAM_FETCHED, stream);
            }
            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_NOT_FOUND, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
        } catch (Exception e) {
            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse findByStreamHeadId(Long id) {
        try {
            List<Stream> streams = streamRepository.findByStreamHeadId(id);

            if (!Objects.isNull(streams)) {
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_OK, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.STREAM_FETCHED, streams);
            }
            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_NOT_FOUND, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
        } catch (Exception e) {
            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse createOrUpdate(StreamRequest streamRequest) {
        try {
            Stream stream = mapToEntity(streamRequest);

            if (streamRequest.getId() == null || streamRequest.getId() == 0) {
                streamRepository.save(stream);
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_CREATED, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.STREAM_CREATED, stream);
            } else {
                Optional<Stream> existingStreamOpt = streamRepository.findById(stream.getId());
                if (existingStreamOpt.isPresent()) {
                    Stream existingStream = existingStreamOpt.get();
                    updateEntity(existingStream, streamRequest);
                    streamRepository.save(existingStream);
                    return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_ACCEPTED, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.STREAM_UPDATED, existingStream);
                } else {
                    return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_NOT_FOUND, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
                }
            }
        } catch (Exception e) {
            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse delete(Long id) {
        try {
            Stream stream = streamRepository.findById(id).orElseThrow(() -> new RuntimeException("Stream not Found"));
            if (stream.getStatus()) {
                stream.setStatus(false);
                streamRepository.save(stream);
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_ACCEPTED, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.STREAM_DELETED, stream);
            } else {
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_NOT_FOUND, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.DATA_NOT_FOUND, stream);
            }
        } catch (Exception e) {
            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse deleteByName(String streamName) {
        try {
            Stream stream = streamRepository.findByStreamName(streamName);
            if (stream.getStatus()) {
                stream.setStatus(false);
                streamRepository.save(stream);
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_ACCEPTED, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.STREAM_DELETED, stream);
            } else {
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_NOT_FOUND, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.DATA_NOT_FOUND, stream);
            }
        } catch (Exception e) {
            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }


    private void updateEntity(Stream stream, StreamRequest request) {
        stream.setStreamName(request.getStreamName());
        stream.setStreamHeadId(request.getStreamHeadId());
    }

    private StreamResponse mapToResponse(Stream stream) {
        return new StreamResponse(
                stream.getId(),
                stream.getStreamName(),
                stream.getStatus(),
                stream.getStreamHeadId()
        );
    }

    private Stream mapToEntity(StreamRequest request) {
        return new Stream(
                null,
                request.getStreamName(),
                true,
                request.getStreamHeadId()
        );
    }
}
