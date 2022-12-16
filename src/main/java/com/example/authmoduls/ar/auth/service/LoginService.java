package com.example.authmoduls.ar.auth.service;

import com.example.authmoduls.ar.auth.decorator.*;
import com.example.authmoduls.ar.auth.model.Gender;
import com.example.authmoduls.ar.auth.model.Login;
import com.example.authmoduls.auth.model.Accesss;
import com.example.authmoduls.common.decorator.FilterSortRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface LoginService {
    LoginResponse addOrUpdateUsers(LoginAddRequest loginAddRequest, String id, Accesss accesss, Gender gender) throws InvocationTargetException, IllegalAccessException;

    LoginResponse getUser(String id) throws InvocationTargetException, IllegalAccessException;

    void deleteUser(String id);

    List<LoginResponse> getAllUser() throws InvocationTargetException, IllegalAccessException;

    Page<Login> getAllUserByFilterAndSortAndPage(LoginFilter loginFilter, FilterSortRequest.SortRequest<LoginSortBy> sort, PageRequest pagination);

    LoginResponse getToken(String id) throws InvocationTargetException, IllegalAccessException;

    LoginTokenResponse userLogin(LoginRequest loginRequest) throws NoSuchAlgorithmException, InvocationTargetException, IllegalAccessException;

    String getEncryptPassword(String id) throws InvocationTargetException, IllegalAccessException;

    LoginResponse getValidityOfToken(String token) throws InvocationTargetException, IllegalAccessException;

    String getIdFromToken(String token);

    void logOut(String id);

    Login getLoginModel(String id);


}
