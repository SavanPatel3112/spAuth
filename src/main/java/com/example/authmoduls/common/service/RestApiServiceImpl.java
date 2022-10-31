package com.example.authmoduls.common.service;

import com.example.authmoduls.common.decorator.RestAPI;
import com.example.authmoduls.common.repository.RestAPIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class RestApiServiceImpl implements RestAPIService {

    @Autowired
    RestAPIRepository restApiRepository;

    @Override
    public List<RestAPI> getAll() {
        List<RestAPI> restAPIS = restApiRepository.findAllBy();
        return restAPIS;
    }

    @Override
    public boolean hasAccess(List<String> roles,String name) {
        return restApiRepository.existsByRolesInAndName(roles,name);
    }
}