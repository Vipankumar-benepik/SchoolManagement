package com.school.SchoolManagement.Implementation;
import com.school.SchoolManagement.Dto.Request.BookRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;

import java.util.List;

public interface LibraryImpl {
    BaseApiResponse findAllBooks();
    BaseApiResponse findById(Long id);
    BaseApiResponse findByTitle(String title);
    BaseApiResponse findByAuthor(String author);
    BaseApiResponse createOrUpdateBook(BookRequest libraryRequest);
    BaseApiResponse createMultiple(List<BookRequest> libraryRequest);
    BaseApiResponse deleteBook(Long id);
}
