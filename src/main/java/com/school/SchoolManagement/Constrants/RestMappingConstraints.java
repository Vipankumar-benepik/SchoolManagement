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

        String TEACHER_PROFILE_API = "/teacher";
        String TEACHER_FETCH_API = "/teacher/fetch";
        String TEACHER_FETCH_BY_ID_API = "/teacher/fetchbyid";
        String TEACHER_CREATE_API = "/teacher/create";
        String TEACHER_BATCH_API = "/teacher/batch";
        String TEACHER_DELETE_API = "/teacher/delete";

        String PARENT_PROFILE_API = "/parent";
        String PARENT_FETCH_API = "/parent/fetch";
        String PARENT_FETCH_BY_ID_API = "/parent/fetchbyid";
        String PARENT_FETCH_BY_STUDENT_ID_API = "/parent/fetchbyid";
        String PARENT_CREATE_API = "/parent/create";
        String PARENT_BATCH_API = "/parent/batch";
        String PARENT_DELETE_API = "/parent/delete";

        String BOOK_PROFILE_API = "/book";
        String BOOK_FETCH_API = "/book/fetch";
        String BOOK_FETCH_BY_ID_API = "/book/fetchbyid";
        String BOOK_FETCH_BY_STUDENT_ID_API = "/book/fetchbyid";
        String BOOK_CREATE_API = "/book/create";
        String BOOK_BATCH_API = "/book/batch";
        String BOOK_DELETE_API = "/book/delete";

        String LIBRARIAN_PROFILE_API = "/librarian";
        String LIBRARIAN_FETCH_API = "/librarian/fetch";
        String LIBRARIAN_FETCH_BY_ID_API = "/librarian/fetchbyid";
        String LIBRARIAN_FETCH_BY_STUDENT_ID_API = "/librarian/fetchbyid";
        String LIBRARIAN_CREATE_API = "/librarian/create";
        String LIBRARIAN_BATCH_API = "/librarian/batch";
        String LIBRARIAN_DELETE_API = "/librarian/delete";

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

        // TEACHER API MESSAGE_NAMES
        String TEACHER_CREATED = "Teacher successfully created.";
        String TEACHER_FETCHED = "Teacher successfully fetched.";
        String TEACHER_UPDATED = "Teacher information updated.";
        String TEACHER_DELETED = "Teacher successfully deleted.";
        String TEACHER_NOT_FOUND = "Teacher not Found.";
        String TEACHER_REF_EMAIL_NOT_FOUND = "Teacher Email not Found.";
        String TEACHER_REF_ID_NOT_FOUND = "Teacher Id not Found.";

        // PARENT API MESSAGE_NAMES
        String PARENT_CREATED = "Parent successfully created.";
        String PARENT_FETCHED = "Parent successfully fetched.";
        String PARENT_UPDATED = "Parent information updated.";
        String PARENT_DELETED = "Parent successfully deleted.";
        String PARENT_NOT_FOUND = "Parent not Found.";
        String PARENT_REF_EMAIL_NOT_FOUND = "Parent Email not Found.";
        String PARENT_REF_ID_NOT_FOUND = "Parent Id not Found.";

        // LIBRARIAN API MESSAGE_NAMES
        String LIBRARIAN_CREATED = "Librarian successfully created.";
        String LIBRARIAN_FETCHED = "Librarian successfully fetched.";
        String LIBRARIAN_UPDATED = "Librarian information updated.";
        String LIBRARIAN_DELETED = "Librarian successfully deleted.";
        String LIBRARIAN_NOT_FOUND = "Librarian not Found.";
        String LIBRARIAN_REF_EMAIL_NOT_FOUND = "Librarian Email not Found.";
        String LIBRARIAN_REF_ID_NOT_FOUND = "Librarian Id not Found.";

        // BOOK API MESSAGE_NAMES
        String BOOK_CREATED = "Book successfully created.";
        String BOOK_FETCHED = "Book successfully fetched.";
        String BOOK_UPDATED = "Book information updated.";
        String BOOK_DELETED = "Book successfully deleted.";

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
