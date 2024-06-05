package com.job.sagar.repository;


import java.math.BigInteger;

import com.job.sagar.model.SchedulerData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SchedulerRepository extends PagingAndSortingRepository<SchedulerData, BigInteger>, CrudRepository<SchedulerData, BigInteger> {
    SchedulerData findSchedulerDataBySchedulerName(String name);
}
