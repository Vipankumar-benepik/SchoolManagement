package com.school.SchoolManagement.Service;

import com.school.SchoolManagement.Dto.Request.UserRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Dto.Response.UserResponse;
import com.school.SchoolManagement.Entity.*;
import com.school.SchoolManagement.Implementation.UserImpl;
import com.school.SchoolManagement.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.school.SchoolManagement.Constrants.RestMappingConstraints.*;

@Service
public class UserService implements UserImpl {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private LibrarianRepository librarianRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public BaseApiResponse findAllUser() {
        try {
            List<User> users = userRepository.findAll();

            List<UserResponse> activeUsers = users.stream()
                    .filter(user -> user.getStatus() != null && user.getStatus())
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.USER_FETCHED, activeUsers);

        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    public BaseApiResponse findById(Long id) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not Found"));
            if (user.getStatus()) {
                return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.USER_FETCHED, user);
            } else {
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.USER_NOT_FOUND, Collections.emptyList());
            }
        } catch (Exception e) {
            if (e.getMessage().equals("User not Found")) {
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.USER_NOT_FOUND, Collections.emptyList());
            }
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    public BaseApiResponse findByEmail(String email) {
        try {
            User user = userRepository.findByEmail(email);
            if (user != null && user.getStatus()) {
                return new BaseApiResponse(STATUS_CODES.HTTP_OK, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.USER_FETCHED, user);
            }

            return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.USER_NOT_FOUND, Collections.emptyList());
        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    public BaseApiResponse createOrUpdateUser(UserRequest userRequest) {
        try {
            User user = mapToEntity(userRequest);

            if (userRequest.getId() == null || userRequest.getId() == 0) {
                if (userRequest.getRole().equals(Role.ADMIN)) {
                    Admin existingAdmin = adminRepository.findById(userRequest.getRefId()).orElseThrow(() -> new RuntimeException("Admin not Found"));
                    if (existingAdmin != null && existingAdmin.getEmail().equals(userRequest.getEmail())) {
                        user.setEmail(existingAdmin.getEmail());
                        user.setRole(userRequest.getRole());
                        System.out.println("UserRequest role: " + userRequest.getRole());
                        System.out.println("User Entity role: " + user.getRole());
                        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
                        System.out.println("This is user Password " + user.getPassword());
                        System.out.println("This is password " + passwordEncoder.encode(user.getPassword()));
                        userRepository.save(user);
                        return new BaseApiResponse(STATUS_CODES.HTTP_CREATED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.USER_CREATED, user);
                    } else {
                        if (!existingAdmin.getEmail().equals(userRequest.getEmail())) {
                            return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.ADMIN_REF_EMAIL_NOT_FOUND, Collections.emptyList());
                        }
                        return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.ADMIN_REF_ID_NOT_FOUND, user);
                    }
                }


                else if (userRequest.getRole().equals(Role.STUDENT)) {
                    Student existingStudent = studentRepository.findById(userRequest.getRefId()).orElseThrow(() -> new RuntimeException("Student not Found"));
                    if (existingStudent != null && existingStudent.getEmail().equals(userRequest.getEmail())) {
                        user.setEmail(existingStudent.getEmail());
                        user.setRole(userRequest.getRole());
                        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
                        userRepository.save(user);
                        return new BaseApiResponse(STATUS_CODES.HTTP_CREATED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.USER_CREATED, user);
                    } else {
                        if (!existingStudent.getEmail().equals(userRequest.getEmail())) {
                            return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.STUDENT_REF_EMAIL_NOT_FOUND, Collections.emptyList());
                        }
                        return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.STUDENT_REF_ID_NOT_FOUND, user);
                    }
                }


                else if (userRequest.getRole().equals(Role.TEACHER)) {
                    Teacher existingTeacher = teacherRepository.findById(userRequest.getRefId()).orElseThrow(() -> new RuntimeException("Teacher not Found"));
                    if (existingTeacher != null && existingTeacher.getEmail().equals(userRequest.getEmail())) {
                        user.setEmail(existingTeacher.getEmail());
                        user.setRole(userRequest.getRole());
                        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
                        userRepository.save(user);
                        return new BaseApiResponse(STATUS_CODES.HTTP_CREATED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.TEACHER_CREATED, user);
                    } else {
                        if(!existingTeacher.getEmail().equals(userRequest.getEmail())){
                            return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.TEACHER_REF_EMAIL_NOT_FOUND, Collections.emptyList());
                        }
                        return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.TEACHER_REF_ID_NOT_FOUND, user);
                    }
                }

                else if (userRequest.getRole().equals(Role.PARENT)) {
                    Parent existingParent = parentRepository.findById(userRequest.getRefId()).orElseThrow(() -> new RuntimeException("Parent not Found"));
                    if (existingParent != null && existingParent.getEmail().equals(userRequest.getEmail())) {
                        user.setEmail(existingParent.getEmail());
                        user.setRole(userRequest.getRole());
                        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
                        userRepository.save(user);
                        return new BaseApiResponse(STATUS_CODES.HTTP_CREATED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.PARENT_CREATED, user);
                    } else {
                        if(!existingParent.getEmail().equals(userRequest.getEmail())){
                            return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.PARENT_REF_EMAIL_NOT_FOUND, Collections.emptyList());
                        }
                        return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.PARENT_REF_ID_NOT_FOUND, user);
                    }
                }

                else if (userRequest.getRole().equals(Role.LIBRARIAN)) {
                    Librarian existingLibrarian = librarianRepository.findById(userRequest.getRefId()).orElseThrow(() -> new RuntimeException("Librarian not Found"));
                    if (existingLibrarian != null && existingLibrarian.getEmail().equals(userRequest.getEmail())) {
                        user.setEmail(existingLibrarian.getEmail());
                        user.setRole(userRequest.getRole());
                        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
                        System.out.println(user.toString());
                        userRepository.save(user);
                        return new BaseApiResponse(STATUS_CODES.HTTP_CREATED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.LIBRARIAN_CREATED, user);
                    } else {
                        if(!existingLibrarian.getEmail().equals(userRequest.getEmail())){
                            return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.LIBRARIAN_REF_EMAIL_NOT_FOUND, Collections.emptyList());
                        }
                        return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.LIBRARIAN_REF_ID_NOT_FOUND, user);
                    }
                }

            } else {
                Optional<User> existingUserOpt = userRepository.findById(userRequest.getId());
                if (existingUserOpt.isPresent()) {
                    User existingUser = existingUserOpt.get();
                    updateEntity(existingUser, userRequest);
                    userRepository.save(existingUser);
                    return new BaseApiResponse(STATUS_CODES.HTTP_ACCEPTED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.USER_UPDATED, existingUser);
                } else {
                    return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.USER_NOT_FOUND, Collections.emptyList());
                }
            }

            return new BaseApiResponse(STATUS_CODES.HTTP_NOT_ACCEPTABLE, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.NOT_ACCEPTABLE, Collections.emptyList());

        } catch (Exception e) {
            if (e.getMessage().equals("Student not Found")) {
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.STUDENT_NOT_FOUND, Collections.emptyList());
            } else if (e.getMessage().equals("Admin not Found")) {
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.ADMIN_NOT_FOUND, Collections.emptyList());
            } else if (e.getMessage().equals("Teacher not Found")) {
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.TEACHER_NOT_FOUND, Collections.emptyList());
            } else if (e.getMessage().equals("Parent not Found")) {
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.PARENT_NOT_FOUND, Collections.emptyList());
            } else if (e.getMessage().equals("Librarian not Found")) {
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.LIBRARIAN_NOT_FOUND, Collections.emptyList());
            }
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, e.getMessage(), Collections.emptyList());
        }
    }

    public BaseApiResponse deleteUser(Long id) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not Found"));
            if (user.getStatus()) {
                user.setStatus(false);
                userRepository.save(user);
                return new BaseApiResponse(STATUS_CODES.HTTP_ACCEPTED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.USER_DELETED, user);
            } else {
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.USER_NOT_FOUND, user);
            }

        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    public BaseApiResponse deleteUserByEmail(String email) {
        try {
            User user = userRepository.findByEmail(email);
            if (user.getStatus()) {
                user.setStatus(false);
                userRepository.save(user);
                return new BaseApiResponse(STATUS_CODES.HTTP_ACCEPTED, SUCCESS_STATUS.SUCCESS, MESSAGE_NAMES.USER_DELETED, user);
            } else {
                return new BaseApiResponse(STATUS_CODES.HTTP_NOT_FOUND, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.USER_NOT_FOUND, user);
            }

        } catch (Exception e) {
            return new BaseApiResponse(STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, SUCCESS_STATUS.FAILURE, MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    private void updateEntity(User user, UserRequest request) {
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        user.setRefId(request.getRefId());
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getRefId()
        );
    }

    private User mapToEntity(UserRequest request) {
        return new User(
                null,
                null,
                request.getPassword(),
                true,
                request.getRole(),
                request.getRefId()
        );
    }
}
