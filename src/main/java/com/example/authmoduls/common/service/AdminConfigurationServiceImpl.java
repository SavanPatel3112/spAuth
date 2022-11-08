package com.example.authmoduls.common.service;

import com.example.authmoduls.auth.model.UserModel;
import com.example.authmoduls.common.decorator.AdminResponse;
import com.example.authmoduls.common.decorator.NullAwareBeanUtilsBean;
import com.example.authmoduls.common.model.AdminConfiguration;
import com.example.authmoduls.common.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;

@Service
public class AdminConfigurationServiceImpl implements AdminConfigurationService {

    @Autowired
    AdminRepository adminRepository;
    @Autowired
    NullAwareBeanUtilsBean nullAwareBeanUtilsBean;

    @Override
    public AdminResponse addConfiguration() throws InvocationTargetException, IllegalAccessException {
        AdminConfiguration adminConfiguration = new AdminConfiguration();
        if (!CollectionUtils.isEmpty(adminRepository.findAll())){
           adminConfiguration = adminRepository.findAll().get(0);
        }else{
           adminConfiguration = adminRepository.save(adminConfiguration);
        }
        AdminResponse adminResponse= new AdminResponse();
        nullAwareBeanUtilsBean.copyProperties(adminResponse,adminConfiguration);
        return adminResponse;
    }

    @Override
    public AdminConfiguration getConfigurationDetails() throws InvocationTargetException, IllegalAccessException {
        AdminConfiguration adminConfiguration=new AdminConfiguration();
        if (adminRepository.findAll().isEmpty()){
            adminRepository.save(adminConfiguration) ;
        }else {
            AdminConfiguration adminConfiguration1 = adminRepository.findAll().get(0);
            nullAwareBeanUtilsBean.copyProperties(adminConfiguration1,adminConfiguration);
            adminRepository.save(adminConfiguration1);
        }
        return adminConfiguration;
    }

}
