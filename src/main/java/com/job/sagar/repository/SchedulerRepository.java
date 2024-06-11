package com.job.sagar.repository;


import java.math.BigInteger;

import com.job.sagar.model.SchedulerData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchedulerRepository extends PagingAndSortingRepository<SchedulerData, BigInteger> {
    SchedulerData findSchedulerDataBySchedulerName(String name);
}
