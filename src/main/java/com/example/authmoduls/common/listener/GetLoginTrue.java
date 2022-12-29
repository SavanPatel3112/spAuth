package com.example.authmoduls.common.listener;

import com.example.authmoduls.ar.auth.repository.LoginRepository;
import com.example.authmoduls.ar.auth.service.LoginService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class GetLoginTrue extends QuartzJobBean {

    private final LoginRepository loginRepository;
    private final LoginService loginService;

    public GetLoginTrue(LoginRepository loginRepository, LoginService loginService) {
        this.loginRepository = loginRepository;
        this.loginService = loginService;
    }
/*
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        List<Login> logins = loginRepository.findAllByLoginTrue();
        for (Login login : logins) {
            login.setLogin(false);
            Date date = new Date();
            login.setLogoutTime(date);
            loginRepository.save(login);
        }
    } */

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        try {
            new Thread(loginService::logOut).start();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
