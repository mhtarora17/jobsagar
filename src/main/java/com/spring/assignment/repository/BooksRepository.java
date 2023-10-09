package com.spring.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.assignment.model.Book;

/**
 * The interface User repository.
 *
 * @author Mohit Arora
 */
@Repository
public interface BooksRepository extends JpaRepository<Book, Long> {}
