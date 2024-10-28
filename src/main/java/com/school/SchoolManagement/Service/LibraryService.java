package com.school.SchoolManagement.Service;

import com.school.SchoolManagement.Constrants.RestMappingConstraints;
import com.school.SchoolManagement.Dto.Request.BookRequest;
import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Dto.Response.BookResponse;
import com.school.SchoolManagement.Entity.Book;
import com.school.SchoolManagement.Implementation.LibraryImpl;
import com.school.SchoolManagement.Repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibraryService implements LibraryImpl {
    @Autowired
    private LibraryRepository libraryRepository;

    @Override
    public BaseApiResponse findAllBooks() {
        try {
            List<Book> libraries = libraryRepository.findAll();

            List<BookResponse> activeBook = libraries.stream()
                    .filter(book -> book.getStatus() != null && book.getStatus())
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_OK, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.BOOK_FETCHED, activeBook);

        } catch (Exception e) {
            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse findById(Long id) {
        try{
            Book book = libraryRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not Found"));
            if (book.getStatus()) {
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_OK, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.BOOK_FETCHED, book);
            }
            else {
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_NOT_FOUND, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
            }
        } catch (Exception e) {
            if(e.getMessage().equals("Book not Found")){
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_NOT_FOUND, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
            }
            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse findByTitle(String title) {
        try{
            Book book = libraryRepository.findByTitle(title);
            if (book != null && book.getStatus()) {
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_OK, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.BOOK_FETCHED, book);
            }

            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_NOT_FOUND, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
        } catch (Exception e) {
            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse findByAuthor(String author){
        try{
            Book book = libraryRepository.findByAuthor(author);
            if (book != null && book.getStatus()) {
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_OK, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.BOOK_FETCHED, book);
            }

            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_NOT_FOUND, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
        } catch (Exception e) {
            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse createOrUpdateBook(BookRequest bookRequest) {
        try{
            Book book = mapToEntity(bookRequest);

            if (bookRequest.getId() == null || bookRequest.getId() == 0) {
                libraryRepository.save(book);
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_CREATED, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.BOOK_CREATED, book);
            } else {
                Optional<Book> existingBooksOpt = libraryRepository.findById(bookRequest.getId());
                if (existingBooksOpt.isPresent()) {
                    Book existingBook = existingBooksOpt.get();
                    updateEntity(existingBook, bookRequest);
                    libraryRepository.save(existingBook);
                    return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_ACCEPTED, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.BOOK_UPDATED, existingBook);
                } else {
                    return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_NOT_FOUND, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.DATA_NOT_FOUND, Collections.emptyList());
                }
            }
        } catch (Exception e) {
            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse createMultiple(List<BookRequest> bookRequests) {
        try{
            List<Book> libraries = new ArrayList<>();
            for (BookRequest request: bookRequests){
                if (request.getId() == null || request.getId() == 0){
                    Book book = mapToEntity(request);
                    libraries.add(book);
                }
                else{
                    return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_NOT_ACCEPTABLE, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.ID_NOT_ACCEPTABLE, Collections.emptyList());
                }
            }
            libraryRepository.saveAll(libraries);
            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_CREATED, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.BOOK_CREATED, libraries);
        }catch (Exception e){
            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }

    @Override
    public BaseApiResponse deleteBook(Long id) {
        try{
            Book book = libraryRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not Found"));
            if(book.getStatus()){
                book.setStatus(false);
                libraryRepository.save(book);
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_ACCEPTED, RestMappingConstraints.SUCCESS_STATUS.SUCCESS, RestMappingConstraints.MESSAGE_NAMES.BOOK_DELETED, book);
            }
            else{
                return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_NOT_FOUND, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.DATA_NOT_FOUND, book);
            }

        }
        catch (Exception e){
            return new BaseApiResponse(RestMappingConstraints.STATUS_CODES.HTTP_INTERNAL_SERVER_ERROR, RestMappingConstraints.SUCCESS_STATUS.FAILURE, RestMappingConstraints.MESSAGE_NAMES.SOMETHING_WENT_WRONG, Collections.emptyList());
        }
    }


    private void updateEntity(Book book, BookRequest request) {
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setQuantity(request.getQuantity());
//        book.setPassword(request.getPassword());
    }

    private BookResponse mapToResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book .getQuantity()
        );
    }

    private Book mapToEntity(BookRequest request) {
        return new Book(
                null,
                request.getTitle(),
                request.getAuthor(),
                request.getQuantity(),
                true
        );
    }
}
