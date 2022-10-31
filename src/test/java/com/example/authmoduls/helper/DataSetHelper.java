package com.example.authmoduls.helper;

import com.example.authmoduls.auth.decorator.UserResponse;
import com.example.authmoduls.auth.model.UserModel;
import com.example.authmoduls.auth.repository.userRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataSetHelper {
    @Autowired
    UserHelper userHelper;
    @Autowired
    UserRepository userRepository;

    public void init(){
        userRepository.saveAll(userHelper.userHelper());
    }
    public void cleanUp(){
        userRepository.deleteAll();
    }
    public UserModel getUserModel(){
        return userRepository.findAll().get(0);
    }
}
