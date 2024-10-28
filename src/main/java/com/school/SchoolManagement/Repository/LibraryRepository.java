package com.school.SchoolManagement.Repository;

import com.school.SchoolManagement.Dto.Response.BaseApiResponse;
import com.school.SchoolManagement.Entity.Book;
import com.school.SchoolManagement.Entity.Parent;
import com.school.SchoolManagement.Utils.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryRepository extends GenericRepository<Book, Long> {
    Book findByTitle(String title);
    Book findByAuthor(String author);
}
