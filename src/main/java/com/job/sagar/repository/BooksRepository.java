package com.job.sagar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.job.sagar.model.Book;

/**
 * The interface User repository.
 *
 * @author Mohit Arora
 */
@Repository
public interface BooksRepository extends JpaRepository<Book, Long> {}
