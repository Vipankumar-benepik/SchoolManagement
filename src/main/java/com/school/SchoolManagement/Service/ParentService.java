package com.school.SchoolManagement.Service;

import com.school.SchoolManagement.Dto.Request.ParentRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Dto.Response.ParentResponse;
import com.school.SchoolManagement.Entity.Parent;
import com.school.SchoolManagement.Implementation.ParentImpl;
import com.school.SchoolManagement.Repository.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.school.SchoolManagement.Constrants.RestMappingConstraints.*;

@Service
public class ParentService implements ParentImpl {
    @Autowired
    private ParentRepository parentRepository;

    @Override
    public BaseApiResponse findAllParent() {
        try {
            List<Parent> parents = parentRepository.findAll();

            List<ParentResponse> activeParents = parents.stream()
                    .filter(parent -> parent.getStatus() != null && parent.getStatus())
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.PARENT_FETCHED, activeParents);
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse findById(Long id) {
        try {
            Parent parent = parentRepository.findById(id).orElseThrow(() -> new RuntimeException("Parent not Found"));
            if (parent.getStatus()) {
                return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.PARENT_FETCHED, parent);
            } else {
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
            }
        } catch (Exception e) {
            if (e.getMessage().equals("Parent not Found")) {
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
            }
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    public BaseApiResponse findByStudentId(Long id){
        return null;
    }

    @Override
    public BaseApiResponse findByEmail(String email) {
        try {
            Parent parent = parentRepository.findByEmail(email);
            if (parent != null && parent.getStatus()) {
                return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.PARENT_FETCHED, parent);
            }
            return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse findByParentName(String username) {
        try {
            List<Parent> parents = parentRepository.findByParentName(username);
            List<ParentResponse> parentList = parents.stream()
                    .filter(parent -> parent.getStatus() != null && parent.getStatus())
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.PARENT_FETCHED, parentList);
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse createOrUpdateParent(ParentRequest parentRequest) {
        try {
            Parent parent = mapToEntity(parentRequest);

            if (parentRequest.getId() == null || parentRequest.getId() == 0) {
                parentRepository.save(parent);
                return new BaseApiResponse(STATUS_CODES.HTTP_CREATED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.PARENT_CREATED, parent);
            } else {
                Optional<Parent> existingParentOpt = parentRepository.findById(parent.getId());
                if (existingParentOpt.isPresent()) {
                    Parent existingParent = existingParentOpt.get();
                    updateEntity(existingParent, parentRequest);
                    parentRepository.save(existingParent);
                    return new BaseApiResponse(STATUS_CODES.HTTP_ACCEPTED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.PARENT_UPDATED, existingParent);
                } else {
                    return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
                }
            }
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse createMultiple(List<ParentRequest> parentRequests){
        try{
            List<Parent> parents = new ArrayList<>();
            for (ParentRequest request: parentRequests){
                if (request.getId() == null || request.getId() == 0){
                    Parent parent1 = mapToEntity(request);
                    parents.add(parent1);
                }
                else{
                    return new BaseApiResponse(STATUS_CODES.HTTP_NOT_ACCEPTABLE, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.ID_NOT_ACCEPTABLE, Collections.emptyList());
                }
            }
            parentRepository.saveAll(parents);
            return new BaseApiResponse(STATUS_CODES.HTTP_CREATED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.PARENT_CREATED, parents);
        }catch (Exception e){
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse deleteParent(Long id) {
        try{
            Parent parent = parentRepository.findById(id).orElseThrow(() -> new RuntimeException("Parent not Found"));
            if(parent.getStatus()){
                parent.setStatus(false);
                parentRepository.save(parent);
                return new BaseApiResponse(STATUS_CODES.HTTP_ACCEPTED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.PARENT_DELETED, parent);
            }
            else{
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.DATA_NOT_FOUND, parent);
            }
        }catch (Exception e){
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse deleteParentByEmail(String email) {
        try{
            Parent parent = parentRepository.findByEmail(email);
            if(parent.getStatus()){
                parent.setStatus(false);
                parentRepository.save(parent);
                return new BaseApiResponse(STATUS_CODES.HTTP_ACCEPTED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.PARENT_DELETED, parent);
            }
            else{
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.DATA_NOT_FOUND, parent);
            }
        }catch (Exception e){
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    private void updateEntity(Parent parent, ParentRequest request) {
        parent.setParentName(request.getParentName());
        parent.setDob(request.getDob());
        parent.setGender(request.getGender());
        parent.setRelation(request.getRelation());
        parent.setAddress(request.getAddress());
        parent.setPhone(request.getPhone());
        parent.setStudentId(request.getStudentId());
//        parent.setPassword(request.getPassword());
    }

    private ParentResponse mapToResponse(Parent parent) {
        return new ParentResponse(
                parent.getId(),
                parent.getParentName(),
                parent.getDob(),
                parent.getGender(),
                parent.getRelation(),
                parent.getAddress(),
                parent.getPhone(),
                parent.getStudentId(),
                parent.getEmail()
        );
    }

    private Parent mapToEntity(ParentRequest request) {
        return new Parent(
                null,
                request.getParentName(),
                request.getDob(),
                request.getGender(),
                request.getRelation(),
                request.getAddress(),
                request.getPhone(),
                true,
                request.getStudentId(),
                request.getEmail()
        );
    }
}
