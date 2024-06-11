package com.job.sagar.dao.impl;

import com.job.sagar.Utils.CommonUtils;
import com.job.sagar.dao.SchedulerServiceDao;
import com.job.sagar.exception.BaseException;
import com.job.sagar.exception.ServiceErrorFactory;
import com.job.sagar.model.SchedulerData;
import com.job.sagar.repository.SchedulerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.job.sagar.constant.ErrorCodesConstant.DB_SERVICE_UNAVAILABLE_MESSAGE;

@Repository
public class SchedulerServiceDaoImpl implements SchedulerServiceDao {

    private static final Logger logger = LogManager.getLogger(SchedulerServiceDaoImpl.class);


    private final SchedulerRepository schedulerRepository;

    @Autowired
    public SchedulerServiceDaoImpl(SchedulerRepository schedulerRepository) {
        this.schedulerRepository = schedulerRepository;
    }

    @Override
    public SchedulerData getSchedulerData (String name) {
        try {
            logger.info("Fetching Schedular data: {}.");
            return schedulerRepository.findSchedulerDataBySchedulerName(name);
        } catch (Exception e) {
            BaseException ex = ServiceErrorFactory.getExceptionV3(e, "getSchedulerData", "jobSagar",
                    String.valueOf(DB_SERVICE_UNAVAILABLE_MESSAGE), null);
            logger.error(
                    "Exception occurred: {} while fetching status. ResponseCode: {}. Message: {}.",
                    CommonUtils.exceptionFormatter(e), ex.getCode(), ex.getMessage());
            throw ex;
        }

    }
    @Override
    public void saveSchedulerData (SchedulerData schedulerData) {
        try {
                logger.debug( "Storing Schedular data: {}.", schedulerData);
                schedulerRepository.save(schedulerData);
                logger.info("Successfully stored Schedular data in DB.");
            } catch (Exception e) {
            BaseException ex = ServiceErrorFactory.getExceptionV3(e, "saveSchedulerData", "jobSagar",
                    String.valueOf(DB_SERVICE_UNAVAILABLE_MESSAGE), null);
            logger.error("Exception occurred: {} while fetching status. ResponseCode: {}. Message: {}.",
                    CommonUtils.exceptionFormatter(e), ex.getCode(), ex.getMessage());
            throw ex;
            }
    }
}
