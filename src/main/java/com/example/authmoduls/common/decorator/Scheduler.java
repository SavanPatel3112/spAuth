package com.example.authmoduls.common.decorator;

import com.example.authmoduls.auth.enums.UserSortBy;
import com.example.authmoduls.auth.service.userService.UserService;
import com.example.authmoduls.common.utils.Access;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class Scheduler {
    @Autowired
    UserService userService;

    @Scheduled(initialDelay = 30000 , fixedDelay = 30000)
    public void expired(){
    }

}
