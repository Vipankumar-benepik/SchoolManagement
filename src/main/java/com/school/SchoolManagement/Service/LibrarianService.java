package com.school.SchoolManagement.Service;

import com.school.SchoolManagement.Dto.Request.LibrarianRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Dto.Response.LibrarianResponse;
import com.school.SchoolManagement.Entity.Librarian;
import com.school.SchoolManagement.Implementation.LibrarianImpl;
import com.school.SchoolManagement.Repository.LibrarianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.school.SchoolManagement.Constrants.RestMappingConstraints.*;

@Service
public class LibrarianService implements LibrarianImpl {
    @Autowired
    private LibrarianRepository librarianRepository;

    @Override
    public BaseApiResponse findAll() {
        try {
            List<Librarian> librarians = librarianRepository.findAll();

            List<LibrarianResponse> activeLibrarians = librarians.stream()
                    .filter(librarian -> librarian.getStatus() != null && librarian.getStatus())
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.LIBRARIAN_FETCHED, activeLibrarians);
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse findById(Long id) {
        try {
            Librarian librarian = librarianRepository.findById(id).orElseThrow(() -> new RuntimeException("Librarian not Found"));
            if (librarian.getStatus()) {
                return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.LIBRARIAN_FETCHED, librarian);
            } else {
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
            }
        } catch (Exception e) {
            if (e.getMessage().equals("Librarian not Found")) {
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
            }
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse findByEmail(String email) {
        try {
            Librarian librarian = librarianRepository.findByEmail(email);
            if (librarian != null && librarian.getStatus()) {
                return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.LIBRARIAN_FETCHED, librarian);
            }
            return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse findByName(String username) {
        try {
            List<Librarian> librarians = librarianRepository.findByLibrarianName(username);
            List<LibrarianResponse> activeLibrarian = librarians.stream()
                    .filter(librarian -> librarian.getStatus() != null && librarian.getStatus())
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.LIBRARIAN_FETCHED, activeLibrarian);
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse createOrUpdate(LibrarianRequest librarianRequest) {
        try {
            Librarian librarian = mapToEntity(librarianRequest);

            if (librarianRequest.getId() == null || librarianRequest.getId() == 0) {
                librarianRepository.save(librarian);
                return new BaseApiResponse(STATUS_CODES.HTTP_CREATED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.LIBRARIAN_CREATED, librarian);
            } else {
                Optional<Librarian> existingLibrarianOpt = librarianRepository.findById(librarian.getId());
                if (existingLibrarianOpt.isPresent()) {
                    Librarian existingLibrarian = existingLibrarianOpt.get();
                    updateEntity(existingLibrarian, librarianRequest);
                    librarianRepository.save(existingLibrarian);
                    return new BaseApiResponse(STATUS_CODES.HTTP_ACCEPTED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.LIBRARIAN_UPDATED, existingLibrarian);
                } else {
                    return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
                }
            }
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse delete(Long id) {
        try{
            Librarian librarian = librarianRepository.findById(id).orElseThrow(() -> new RuntimeException("Librarian not Found"));
            if(librarian.getStatus()){
                librarian.setStatus(false);
                librarianRepository.save(librarian);
                return new BaseApiResponse(STATUS_CODES.HTTP_ACCEPTED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.LIBRARIAN_DELETED, librarian);
            }
            else{
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.DATA_NOT_FOUND, librarian);
            }
        }catch (Exception e){
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse deleteByEmail(String email) {
        try{
            Librarian librarian = librarianRepository.findByEmail(email);
            if(librarian.getStatus()){
                librarian.setStatus(false);
                librarianRepository.save(librarian);
                return new BaseApiResponse(STATUS_CODES.HTTP_ACCEPTED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.LIBRARIAN_DELETED, librarian);
            }
            else{
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.DATA_NOT_FOUND, librarian);
            }
        }catch (Exception e){
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    private void updateEntity(Librarian librarian, LibrarianRequest request) {
        librarian.setLibrarianName(request.getLibrarianName());
        librarian.setDob(request.getDob());
        librarian.setGender(request.getGender());
        librarian.setAddress(request.getAddress());
        librarian.setPhone(request.getPhone());
        librarian.setSalary(request.getSalary());
        librarian.setHireDate(request.getHireDate());
//        librarian.setPassword(request.getPassword());
    }

    private LibrarianResponse mapToResponse(Librarian librarian) {
        return new LibrarianResponse(
                librarian.getId(),
                librarian.getLibrarianName(),
                librarian.getDob(),
                librarian.getGender(),
                librarian.getAddress(),
                librarian.getPhone(),
                librarian.getSalary(),
                librarian.getHireDate(),
                librarian.getEmail()
        );
    }

    private Librarian mapToEntity(LibrarianRequest request) {
        return new Librarian(
                null,
                request.getLibrarianName(),
                request.getDob(),
                request.getGender(),
                request.getAddress(),
                request.getPhone(),
                request.getSalary(),
                new Date(),
                true,
                request.getEmail()
        );
    }
}
