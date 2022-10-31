package com.example.authmoduls.common.service;

import com.example.authmoduls.common.decorator.RestAPI;

import java.util.List;

public interface RestAPIService {

    List<RestAPI> getAll();

    boolean hasAccess(List<String> roles ,String name);
}
