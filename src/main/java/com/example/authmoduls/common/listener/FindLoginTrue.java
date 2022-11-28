package com.example.authmoduls.common.listener;

import com.example.authmoduls.auth.model.UserModel;
import com.example.authmoduls.auth.repository.userRepository.UserRepository;
import com.example.authmoduls.common.service.SchedulerService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.List;

public class FindLoginTrue extends QuartzJobBean {
    @Autowired
    UserRepository userRepository;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        List<UserModel> userModel = userRepository.findAllByLoginTrue();
        for (UserModel model : userModel) {
            model.setLogin(false);
            Date date = new Date();
            model.setLogoutTime(date);
            userRepository.save(model);
        }
    }
}
