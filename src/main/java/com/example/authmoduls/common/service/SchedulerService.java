package com.example.authmoduls.common.service;

import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.Date;

@Service
public interface SchedulerService {

    void scheduleOnDate(Class<? extends QuartzJobBean> job, Date date, String name) throws SchedulerException;
    void scheduleCronJob(Class<? extends QuartzJobBean> job, String cron, String name, Time startTime , Time endTime) throws SchedulerException;
    void cancelSchedule(String name) throws SchedulerException;

}
