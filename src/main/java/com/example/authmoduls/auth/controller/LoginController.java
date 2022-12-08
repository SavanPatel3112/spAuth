package com.example.authmoduls.auth.controller;

import com.example.authmoduls.ar.auth.decorator.LoginAddRequest;
import com.example.authmoduls.ar.auth.decorator.LoginFilter;
import com.example.authmoduls.ar.auth.decorator.LoginResponse;
import com.example.authmoduls.ar.auth.decorator.LoginSortBy;
import com.example.authmoduls.ar.auth.model.Gender;
import com.example.authmoduls.ar.auth.model.Login;
import com.example.authmoduls.ar.auth.service.LoginService;
import com.example.authmoduls.common.constant.ResponseConstant;
import com.example.authmoduls.common.decorator.*;
import com.example.authmoduls.common.enums.Role;
import com.example.authmoduls.common.utils.Access;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("/userData")

public class LoginController {
    @Autowired
    LoginService loginService;
    @Autowired
    GeneralHelper generalHelper;

    @Access(levels = {Role.ANONYMOUS})
    @RequestMapping(name = "addOrUpdateUser" , value = "addOrUpdate" , method = RequestMethod.POST)
    public DataResponse<LoginResponse> addOrUpdateUser(@RequestBody LoginAddRequest loginAddRequest , @RequestParam(required = false) String id,  @RequestParam Role role , @RequestParam Gender gender) throws InvocationTargetException, IllegalAccessException {
        DataResponse<LoginResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(loginService.addOrUpdateUser(loginAddRequest,id,role,gender));
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }

    @Access(levels = {Role.ADMIN})
    @RequestMapping(name = "getUser" , value = "getUser/id" , method = RequestMethod.GET)
    public DataResponse<LoginResponse> getUser(String id) throws InvocationTargetException, IllegalAccessException {
        DataResponse<LoginResponse> dataResponse= new DataResponse<>();
        dataResponse.setData(loginService.getUser(id));
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }

    @Access(levels = {Role.ADMIN})
    @RequestMapping(name = "deleteUser" , value = "deleteUser/id" , method = RequestMethod.DELETE)
    public DataResponse<LoginResponse> deleteUser(String id){
        DataResponse<LoginResponse> dataResponse = new DataResponse<>();
        loginService.deleteUser(id);
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }

    @Access(levels = {Role.ADMIN})
    @RequestMapping(name = "getAllUser" , value = "getAllUser" , method = RequestMethod.GET)
    public ListResponse<LoginResponse> getAllUser () throws InvocationTargetException, IllegalAccessException {
        ListResponse<LoginResponse> listResponse = new ListResponse<>();
        listResponse.setData(loginService.getAllUser());
        listResponse.setStatus(Response.getOkResponse());
        return listResponse;
    }

    @SneakyThrows
    @RequestMapping(name = "getAllUserByPagination" , value = "/getALlUser/filter",method = {RequestMethod.POST})
    @Access(levels = {Role.ADMIN})
    public PageResponse<Login> getAllUserByPagination(@RequestBody FilterSortRequest<LoginFilter, LoginSortBy> filterSortRequest){
        PageResponse<Login> pageResponse = new PageResponse<>();
        LoginFilter loginFilter = filterSortRequest.getFilter();
        Page<Login> logins = loginService.getAllUserByFilterAndSortAndPage(loginFilter,filterSortRequest.getSort(),
                generalHelper.getPagination(filterSortRequest.getPage().getPage(),filterSortRequest.getPage().getLimit()));
        pageResponse.setData(logins);
        pageResponse.setStatus(Response.getSuccessResponse(ResponseConstant.SUCCESS));
        return pageResponse;
    }

    @SneakyThrows
    @Access(levels = Role.ANONYMOUS)
    @RequestMapping(name = "getToken", value = "/generateToken", method = RequestMethod.GET)
    public TokenResponse<LoginResponse> getToken(@RequestParam String id) {
        TokenResponse<LoginResponse> tokenResponse = new TokenResponse<>();
        LoginResponse loginResponse = loginService.getToken(id);
        tokenResponse.setData(loginResponse);
        tokenResponse.setStatus(Response.getOkResponse());
        tokenResponse.setToken(loginResponse.getToken());
        return tokenResponse;
    }

    @SneakyThrows
    @Access(levels = Role.ANONYMOUS)
    @RequestMapping(name = "userLogin", value = "/login", method = RequestMethod.GET)
    public DataResponse<Object> userLogin(@RequestParam String email, String password) {
        DataResponse<Object> dataResponse = new DataResponse<>();
        loginService.userLogin(email, password);
        dataResponse.setStatus(Response.getLoginResponse());
        return dataResponse;
    }

    @Access(levels = Role.ANONYMOUS)
    @RequestMapping(name = "getEncryptPassword" , value = "/getEncryptPassword" , method = RequestMethod.GET)
    public DataResponse<String> getEncryptPassword (@RequestParam String id) throws InvocationTargetException, IllegalAccessException {
        DataResponse<String> dataResponse = new DataResponse<>();
        dataResponse.setData(loginService.getEncryptPassword(id));
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }

    @SneakyThrows
    @Access(levels = Role.ANONYMOUS)
    @RequestMapping(name = "getValidityOfToken", value = "/validate/token", method = RequestMethod.GET)
    public TokenResponse<LoginResponse> getValidityOfToken(@RequestParam String token) {
        TokenResponse<LoginResponse> tokenResponse = new TokenResponse<>();
        tokenResponse.setData(loginService.getValidityOfToken(token));
        tokenResponse.setStatus(Response.getTokensucessResponse());
        tokenResponse.setToken(token);
        return tokenResponse;
    }

    @SneakyThrows
    @Access(levels = Role.ANONYMOUS)
    @RequestMapping(name = "getIdFromToken", value = "/getIdFromToken", method = RequestMethod.GET)
    public TokenResponse<String> getIdFromToken(@RequestParam String token) {
        TokenResponse<String> tokenResponse = new TokenResponse<>();
        tokenResponse.setData(loginService.getIdFromToken(token));
        tokenResponse.setStatus(Response.getOkResponse());
        tokenResponse.setToken(token);
        return tokenResponse;
    }

    @SneakyThrows
    @Access(levels = Role.ANONYMOUS)
    @RequestMapping(name = "logOut", value = "/logOut/{id}", method = RequestMethod.GET)
    public DataResponse<Object> logOut(@RequestParam String id) {
        DataResponse<Object> dataResponse = new DataResponse<>();
        loginService.logOut(id);
        dataResponse.setStatus(Response.logOutResponse());
        return dataResponse;
    }

}
