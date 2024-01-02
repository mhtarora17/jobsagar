package com.job.sagar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.job.sagar.model.Users;



@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    // Add custom repository methods here if needed
}