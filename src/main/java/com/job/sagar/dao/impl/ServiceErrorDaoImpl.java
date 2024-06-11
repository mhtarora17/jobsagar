package com.job.sagar.dao.impl;


import java.util.List;

import com.job.sagar.dao.IServiceErrorDao;
import com.job.sagar.exception.BaseException;
import com.job.sagar.exception.ServiceErrorFactory;
import com.job.sagar.model.ServiceError;
import com.job.sagar.repository.ServiceErrorRepository;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation. Autowired;
import org.springframework.stereotype.Repository;

import static com.job.sagar.constant.ErrorCodesConstant.DB_SERVICE_UNAVAILABLE_MESSAGE;

@Repository
public class ServiceErrorDaoImpl implements IServiceErrorDao {
    private ServiceErrorRepository serviceErrorRepository;
    private static final Logger logger = LogManager.getLogger(ServiceErrorDaoImpl.class);

    @Autowired
    public ServiceErrorDaoImpl(ServiceErrorRepository serviceErrorRepository) {
        this.serviceErrorRepository = serviceErrorRepository;
    }

    public Iterable<ServiceError> getAllServiceErrors() {
        try {
            return serviceErrorRepository.findAll();
        } catch (Exception e) {
            logger.error("ex");
            throw e;
        }
    }

    @Override
    public List<ServiceError> findServiceErrorByResponseCode(int responseCode) {
        try {
            logger.info("Fetching Service Error records for Response Code: {}", responseCode);
            List<ServiceError> serviceErrorList = serviceErrorRepository.findServiceErrorByResponseCode(responseCode);
            return serviceErrorList;
        } catch (Exception ex) {
            BaseException e = ServiceErrorFactory.getExceptionV3(ex, "findServiceErrorByResponseCode", "jobSagar",
                    String.valueOf(DB_SERVICE_UNAVAILABLE_MESSAGE), null);
            logger.error("Exception occurred: {} wile fetching Service Error records",
                    ExceptionUtils.getStackTrace(ex));
            throw e;
        }
    }
}