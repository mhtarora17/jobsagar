package com.job.sagar.dao;

import com.job.sagar.model.ServiceError;

import java.util.List;

public interface IServiceErrorDao {

    public Iterable<ServiceError> getAllServiceErrors();

    public List<ServiceError> findServiceErrorByResponseCode(int responseCode);
}
