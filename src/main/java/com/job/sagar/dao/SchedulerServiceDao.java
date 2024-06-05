package com.job.sagar.dao;

import com.job.sagar.model.SchedulerData;

public interface SchedulerServiceDao {

    SchedulerData getSchedulerData(String name);

    void saveSchedulerData(SchedulerData data);
}