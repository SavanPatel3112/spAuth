package com.example.authmoduls.common.service;

import org.apache.logging.log4j.core.config.Scheduled;
import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface SchedulerService {

    void scheduleOnDate(Class<? extends QuartzJobBean> job, Date date, String name) throws SchedulerException;
    void scheduleCronJob(Class<? extends QuartzJobBean> job, String cron, String name,Date startDate, Date endDate) throws SchedulerException;
    void cancelSchedule(String name) throws SchedulerException;

}
