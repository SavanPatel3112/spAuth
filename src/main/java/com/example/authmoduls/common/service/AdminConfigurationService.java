package com.example.authmoduls.common.service;

import com.example.authmoduls.auth.model.UserModel;
import com.example.authmoduls.common.decorator.AdminResponse;
import com.example.authmoduls.common.model.AdminConfiguration;

import java.lang.reflect.InvocationTargetException;

public interface AdminConfigurationService {
    AdminResponse addConfiguration() throws InvocationTargetException, IllegalAccessException;

    AdminConfiguration getConfigurationDetails() throws InvocationTargetException, IllegalAccessException;


}
