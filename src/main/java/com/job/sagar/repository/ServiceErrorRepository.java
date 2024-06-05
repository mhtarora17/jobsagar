package com.job.sagar.repository;

import com.job.sagar.Utils.Pair;
import com.job.sagar.model.ServiceError;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ServiceErrorRepository extends CrudRepository<ServiceError, Pair<String, String>> {

    List<ServiceError> findServiceErrorByResponseCode(int responseCode);
}
