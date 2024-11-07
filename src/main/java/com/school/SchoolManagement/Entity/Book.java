package com.school.SchoolManagement.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Title cannot be empty")
    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @NotNull(message = "Quantity cannot be empty")
    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "status")
    private Boolean status;
}
