package com.school.SchoolManagement.Constrants;

public interface RestMappingConstraints {
    String BASE_URL = "/api/schoolmanagement";

    interface DEFINE_API {
        String LOGIN_BASE_API = "/api/auth";
        String LOGIN_API = "/login";

        String USER_REGISTER_API = "/user/register";
        String USER_FETCH_API = "/user/fetch";
        String USER_FETCH_BY_ID_API = "/user/fetchbyid";
        String USER_DELETE_API = "/user/delete";

        String ADMIN_PROFILE_API = "/admin";
        String ADMIN_FETCH_API = "/admin/fetch";
        String ADMIN_FETCH_BY_ID_API = "/admin/fetchbyid";
        String ADMIN_CREATE_API = "/admin/create";
        String ADMIN_BATCH_API = "/admin/batch";
        String ADMIN_DELETE_API = "/admin/delete";


        String STUDENT_PROFILE_API = "/student";
        String STUDENT_FETCH_API = "/student/fetch";
        String STUDENT_FETCH_BY_ID_API = "/student/fetchbyid";
        String STUDENT_CREATE_API = "/student/create";
        String STUDENT_BATCH_API = "/student/batch";
        String STUDENT_DELETE_API = "/student/delete";

    }

    interface MESSAGE_NAMES {
        // COMMON MESSAGES
        String ID_NOT_ACCEPTABLE = "Id's Not Acceptable";
        String NOT_ACCEPTABLE = "Id's Not Acceptable";

        // USER API MESSAGE_NAMES
        String USER_CREATED = "User successfully created.";
        String USER_FETCHED = "User successfully fetched.";
        String USER_UPDATED = "User information updated.";
        String USER_DELETED = "User successfully deleted.";
        String USER_NOT_FOUND = "User not Found with these credentials.";


        // ADMIN API MESSAGE_NAMES
        String ADMIN_CREATED = "Admin successfully created.";
        String ADMIN_FETCHED = "Admin successfully fetched.";
        String ADMIN_UPDATED = "Admin information updated.";
        String ADMIN_DELETED = "Admin successfully deleted.";
        String ADMIN_NOT_FOUND = "Admin not Found.";
        String ADMIN_REF_EMAIL_NOT_FOUND = "Admin Email not Found.";
        String ADMIN_REF_ID_NOT_FOUND = "Admin Id not Found.";

        // STUDENT API MESSAGE_NAMES
        String STUDENT_CREATED = "Student successfully created.";
        String STUDENT_FETCHED = "Student successfully fetched.";
        String STUDENT_UPDATED = "Student information updated.";
        String STUDENT_DELETED = "Student successfully deleted.";
        String STUDENT_NOT_FOUND = "Student not Found.";
        String STUDENT_REF_EMAIL_NOT_FOUND = "Student Email not Found.";
        String STUDENT_REF_ID_NOT_FOUND = "Student Id not Found.";

        // COMMON STATUS_MESSAGES
        String OPERATION_SUCCESS = "Operation completed successfully.";
        String OPERATION_FAILED = "Operation failed. Please try again.";
        String SOMETHING_WENT_WRONG = "Something went wrong.";
        String INVALID_REQUEST = "The request is invalid.";
        String UNAUTHORIZED_ACCESS = "You are not authorized to access this resource.";
        String SERVER_ERROR = "An internal server error occurred.";
        String FIELD_REQUIRED_MESSAGE = "Field is required.";
        String DATA_NOT_FOUND = "Data not found.";
        String ACCESS_DENIED = "You don't have permission to access this resource.";
        String CHECK_CREDENTIALS = "Please check your credentials.";
    }

    interface SUCCESS_STATUS {
        int SUCCESS = 1;
        int FAILURE = 0;
    }

    interface STATUS_CODES {
        int HTTP_OK = 200;
        int HTTP_CREATED = 201;
        int HTTP_ACCEPTED = 201;
        int HTTP_NO_CONTENT = 204;
        int HTTP_BAD_REQUEST = 400;
        int HTTP_UNAUTHORIZED = 401;
        int HTTP_FORBIDDEN = 403;
        int HTTP_NOT_FOUND = 404;
        int HTTP_NOT_ACCEPTABLE = 406;
        int HTTP_INTERNAL_SERVER_ERROR = 500;
    }

}
