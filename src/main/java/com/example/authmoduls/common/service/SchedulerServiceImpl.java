package com.example.authmoduls.common.service;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

public class SchedulerServiceImpl implements SchedulerService {

    @Autowired
    private Scheduler scheduler;
    @Override
    public void scheduleOnDate(Class<? extends QuartzJobBean> job, Date date, String name) throws SchedulerException {
        JobDetail jobDetail = buildJobDetail(job, name);
        Trigger trigger = buildJobTrigger(jobDetail, date);
        if (scheduler.checkExists(jobDetail.getKey())){
            scheduler.deleteJob(jobDetail.getKey());
        }
        scheduler.scheduleJob(jobDetail, trigger);
    }



    @Override
    public void scheduleCronJob(Class<? extends QuartzJobBean> job, String cron, String name, Date startDate, Date endDate) throws SchedulerException {
        JobDetail jobDetail = buildJobDetail(job, name);
        if(startDate==null && endDate==null){
            startDate = new Date();
        }

    }

    @Override
    public void cancelSchedule(String name) throws SchedulerException {
        scheduler.pauseJob(new JobKey(name));
        scheduler.deleteJob(new JobKey(name));
    }

    private JobDetail buildJobDetail(Class<? extends QuartzJobBean> job, String name) {
        JobDataMap jobDataMap = new JobDataMap();
        return JobBuilder.newJob()
                .withIdentity(name)
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }
    private Trigger buildJobTrigger(JobDetail jobDetail, Date startAt) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .startAt(startAt)
                .withIdentity(jobDetail.getKey().getName())
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }
    private Trigger buildJobTrigger(JobDetail jobDetail, String cronString) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName())
                .withSchedule(CronScheduleBuilder.cronSchedule(cronString))
                .build();
    }
    private Trigger buildJobTrigger(JobDetail jobDetail, String cronString,Date startDate,Date endDate) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .startAt(startDate)
                .endAt(endDate)
                .withIdentity(jobDetail.getKey().getName())
                .withSchedule(CronScheduleBuilder.cronSchedule(cronString))
                .build();
    }
}
