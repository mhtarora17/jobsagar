package com.job.sagar.repository;

import com.job.sagar.model.BooksInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface User repository.
 *
 * @author Mohit Arora
 */
@Repository
public interface BooksInventoryRepository extends JpaRepository<BooksInventory, Long> {}
