package com.job.sagar.repository;

import com.job.sagar.model.Jobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobsRepository extends JpaRepository<Jobs, Integer> {
    // Add custom query methods if needed
}