package com.example.authmoduls.auth;

import com.example.authmoduls.auth.decorator.*;
import com.example.authmoduls.auth.enums.UserSortBy;
import com.example.authmoduls.auth.model.Address;
import com.example.authmoduls.auth.model.UserModel;
import com.example.authmoduls.common.decorator.*;
import com.example.authmoduls.common.enums.CustomHTTPHeaders;
import com.example.authmoduls.common.enums.Role;
import com.example.authmoduls.common.model.JWTUser;
import com.example.authmoduls.common.utils.JwtTokenUtil;
import com.example.authmoduls.helper.DataSetHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Slf4j
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    DataSetHelper dataSetHelper;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    public void getDataSetHelper() {
        dataSetHelper.cleanUp();
        dataSetHelper.init();
    }
    @Test
    void getUser() throws Exception {
        UserModel userModel =dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        MvcResult mvcResult=mockMvc.perform(get("/users/get/id/?id="+userModel.getId())
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void getDeleteUser() throws Exception {

        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        MvcResult mvcResult = mockMvc.perform(get("/users/delete/id/?id="+userModel.getId())
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void getToken() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        MvcResult mvcResult = mockMvc.perform(get("/users/generateToken?id="+userModel.getId())
                .header(CustomHTTPHeaders.TOKEN.toString(),token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void getUpdateUser() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        UserAddRequest userAddRequest = new UserAddRequest();
        userAddRequest.setUserName("pk1402");
        userAddRequest.setEmail("pk@102.com");
        userAddRequest.setFirstName("Pinkesh");
        userAddRequest.setMiddleName("Kiritbhai");
        userAddRequest.setLastName("Patel");
        userAddRequest.setPassword("Pk@1402");
        userAddRequest.setAddress((List<Address>) new Address("f-7 neelDip flat","guruKulRoad","memNagar","ahmedabad","gujarat","380062"));
        String json = new Gson().toJson(userAddRequest);
        MvcResult mvcResult = mockMvc.perform(post("/users/addOrUpdate?id="+userModel.getId())
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk()).andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());

    }
    @Test
    void getEncryptPassword() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        MvcResult mvcResult = mockMvc.perform(get("/users/getEncryptPassword?id="+userModel.getId())
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void getCheckUserAuthentication() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        MvcResult mvcResult = mockMvc.perform(get("/users/getPasswords?email="+userModel.getEmail()+"&password="+userModel.getPassWord())
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void getIdFromToken() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        MvcResult mvcResult = mockMvc.perform(get("/users/getIdFromToken?token="+token)
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        TokenResponse tokenResponse = new Gson().fromJson(result,new TypeToken<TokenResponse>(){}.getType());
        Assertions.assertEquals("Ok", tokenResponse.getStatus().getStatus(), tokenResponse.getStatus().getDescription());
    }
    @Test
    void getValidityOfToken() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        MvcResult mvcResult = mockMvc.perform(get("/users/validate/token?token="+token)
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        TokenResponse tokenResponse = new Gson().fromJson(result,new TypeToken<TokenResponse>(){}.getType());
        Assertions.assertEquals("Ok", tokenResponse.getStatus().getStatus(), tokenResponse.getStatus().getDescription());
    }
    @Test
    void userLogin() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        MvcResult mvcResult = mockMvc.perform(get("/users/login?email="+userModel.getEmail()+"&password="+userModel.getPassWord())
                .header(CustomHTTPHeaders.TOKEN.toString(),token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void getOtp() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        MvcResult mvcResult = mockMvc.perform(get("/users/verification/Otp?id="+userModel.getId()+"&otp="+userModel.getOtp())
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void forgotPassword() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        MvcResult mvcResult = mockMvc.perform(get("/users/forgotPassword?email="+userModel.getEmail())
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void otpVerifications() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        MvcResult mvcResult = mockMvc.perform(get("/users/otp/verification?id="+userModel.getId()+"&otp="+userModel.getOtp())
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void setPassword() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        MvcResult mvcResult = mockMvc.perform(get("/users/setPassword?confirmPassword="+userModel.getPassWord()+"&id="+ userModel.getId()+"&password="+userModel.getPassWord())
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void changePassword() throws Exception{
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        MvcResult mvcResult = mockMvc.perform(get("/users/changePassword?confirmPassword="+userModel.getConfirmPassWord()+"&id="+userModel.getId()+"&newPassword="+ userModel.getNewPassWord()+"&password="+userModel.getPassWord())
                .header(CustomHTTPHeaders.TOKEN.toString(),token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void getUserPassword() throws Exception{
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        MvcResult mvcResult = mockMvc.perform(get("/users/username/password?confirmPassword="+userModel.getConfirmPassWord()+"&password="+userModel.getPassWord()+"&userName="+ userModel.getUserName())
                .header(CustomHTTPHeaders.TOKEN.toString(),token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void logOut() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        MvcResult mvcResult = mockMvc.perform(get("/users/forgotPassword?email="+userModel.getId())
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void getUserByRole() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();

        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        UserFilter userFilter = new UserFilter();
        userFilter.setRole(Role.ADMIN);
        String json = new Gson().toJson(userFilter);
        MvcResult mvcResult = mockMvc.perform(post("/users/testGetUser?role="+userModel.getRole())
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        ListResponse listResponse = new Gson().fromJson(result,new TypeToken<ListResponse>(){}.getType());
        Assertions.assertEquals("Ok", listResponse.getStatus().getStatus(), listResponse.getStatus().getDescription());
    }
    @Test
    void addResult() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();

        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        Result result = new Result();
        result.setSemester(1);
        result.setSpi(9.05);
        result.setYear(2021);
        String json = new Gson().toJson(result);
        MvcResult mvcResult = mockMvc.perform(post("/users/add/Result?id="+userModel.getId())
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk()).andReturn();
        String results = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(results,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void getUserResult() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        UserDetail userDetail = new UserDetail();
        userDetail.setUserIds(Collections.singleton(userModel.getId()));
        userDetail.setSemester(1);
        String json = new Gson().toJson(userDetail);
        MvcResult mvcResult = mockMvc.perform(post("/users/result")
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        ListResponse listResponse = new Gson().fromJson(result,new TypeToken<ListResponse>(){}.getType());
        Assertions.assertEquals("Ok", listResponse.getStatus().getStatus(), listResponse.getStatus().getDescription());
    }
    @Test
    void getUserResultBySemester() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(), Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        UserResult userResult = new UserResult();
        userResult.setUserIds(Collections.singleton(userModel.getId()));
        userResult.setSemester(Collections.singleton(1));
        String json = new Gson().toJson(userResult);
        MvcResult mvcResult = mockMvc.perform(post("/users/resultBySemester")
                        .header(CustomHTTPHeaders.TOKEN.toString(), token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        ListResponse listResponse = new Gson().fromJson(result, new TypeToken<ListResponse>() {
        }.getType());
        Assertions.assertEquals("Ok", listResponse.getStatus().getStatus(), listResponse.getStatus().getDescription());
    }
    @Test
    void getUserResultByMinMaxMark() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        UserIdsRequest userIdsRequest = new UserIdsRequest();
        userIdsRequest.setUserId(Collections.singleton(userModel.getId()));
        String json = new Gson().toJson(userIdsRequest);
        MvcResult mvcResult = mockMvc.perform(post("/users/resultByMinMaxMark?=")
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        ListResponse listResponse = new Gson().fromJson(result,new TypeToken<ListResponse>(){}.getType());
        Assertions.assertEquals("Ok", listResponse.getStatus().getStatus(), listResponse.getStatus().getDescription());
    }
    @Test
    void deleteUserResult() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        MvcResult mvcResult = mockMvc.perform(get("/users/delete/result?id="+userModel.getId()+"?semester=")
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void getUserResultByDate() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        UserResultByDate userResultByDate = new UserResultByDate();
        userResultByDate.setDate(userModel.getDate().toString());
        String json = new Gson().toJson(userResultByDate);
        MvcResult mvcResult = mockMvc.perform(post("/users/resultByDate")
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void updateUserResult() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        Resultupdate resultupdate = new Resultupdate();
        int semester;
        semester=2;
        resultupdate.setDate(userModel.getDate());
        resultupdate.setSpi(4.2);
        resultupdate.setYear(2021);
        String json = new Gson().toJson(resultupdate);
        MvcResult mvcResult = mockMvc.perform(post("/users/update/result?id="+userModel.getId()+"&semester="+semester)
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void resultDetailByEmail() throws Exception{
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        MvcResult mvcResult = mockMvc.perform(get("/users/send/result/email?id="+userModel.getId())
                .header(CustomHTTPHeaders.TOKEN.toString(),token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void userUpdate() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        UserAddRequest userAddRequest = new UserAddRequest();
        userAddRequest.setMiddleName("Pinkesh");
        String json = new Gson().toJson(userAddRequest);
        MvcResult mvcResult = mockMvc.perform(post("/users/get/id/role?id="+userModel.getId()+"&role="+userModel.getRole())
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void userDelete() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        MvcResult mvcResult = mockMvc.perform(get("/users/delete/id?id=?role="+userModel.getId()+"\nrole:"+userModel.getRole())
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void getUserByExcel() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        FilterSortRequest<UserFilter, UserSortBy> filterSortRequest = new FilterSortRequest<>();
        UserFilter userFilter = filterSortRequest.getFilter();
        userFilter.setRole(userFilter.getRole());

        FilterSortRequest.SortRequest<UserSortBy> sort= new FilterSortRequest.SortRequest<>();
        sort.setSortBy(UserSortBy.EMAIL);
        sort.setOrderBy(Sort.Direction.ASC);
        Pagination pagination = new Pagination();
        pagination.setPage(1);
        pagination.setLimit(10);
        String json = new Gson().toJson(filterSortRequest);
        MvcResult mvcResult = mockMvc.perform(get("/users/get/user/excel")
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void uploadFile() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        File file = new File("E:\\java\\randomdata.xlsx");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(),file.getName(),MediaType.MULTIPART_FORM_DATA.toString(), IOUtils.toByteArray(inputStream));
        MvcResult mvcResult = mockMvc.perform(post("/users/rest/upload"+multipartFile)
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void importUsers() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        File file = new File("E:\\java\\randomdata.xlsx");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(),file.getName(),MediaType.MULTIPART_FORM_DATA.toString(), IOUtils.toByteArray(inputStream));
        System.out.println("Multipartfile:"+multipartFile);
        MvcResult mvcResult = mockMvc.perform(post("/users/import"+"?value="+multipartFile)
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void importUsersVerify() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        UserImportVerifyRequest userImportVerifyRequest = new UserImportVerifyRequest();
        userImportVerifyRequest.setId(Collections.singleton(userModel.getUserId()).toString());
        MvcResult mvcResult = mockMvc.perform(post("/users/import/user/verify")
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void sendMailToInvitedUser() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        MvcResult mvcResult = mockMvc.perform(post("/users/sendMail?userStatus=ACTIVE"+userModel.getUserStatus())
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void getAllUserByPagination() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        FilterSortRequest<UserFilter , UserSortBy> filterSortRequest = new FilterSortRequest<>();
        UserFilter userFilter = new UserFilter();
        userFilter.setRole(userFilter.getRole());
        FilterSortRequest.SortRequest<UserSortBy> sort= new FilterSortRequest.SortRequest<>();
        Pagination pagination = new Pagination();
        pagination.setPage(1);
        pagination.setLimit(10);
        sort.setSortBy(UserSortBy.EMAIL);
        sort.setOrderBy(Sort.Direction.ASC);
        filterSortRequest.setPage(pagination);
        filterSortRequest.setSort(sort);
        filterSortRequest.setFilter(userFilter);
        String json = new Gson().toJson(filterSortRequest);
        MvcResult mvcResult = mockMvc.perform(post("/users/getALlUser/filter?=")
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        PageResponse pageResponse = new Gson().fromJson(result,new TypeToken<PageResponse>(){}.getType());
        Assertions.assertEquals("Ok", pageResponse.getStatus().getStatus(), pageResponse.getStatus().getDescription());
    }
    @Test
    void getUserBookDetailsByPagination() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        FilterSortRequest<UserIdsRequest, UserDataSortBy> filterSortRequest = new FilterSortRequest<>();
        FilterSortRequest.SortRequest<UserDataSortBy> sort= new FilterSortRequest.SortRequest<>();
        UserIdsRequest userIdsRequest = new UserIdsRequest();
        userIdsRequest.setUserId(Collections.singleton(userModel.getId()));
        Pagination pagination = new Pagination();
        pagination.setLimit(10);
        pagination.setPage(1);
        sort.setSortBy(UserDataSortBy.FULL_NAME);
        sort.setOrderBy(Sort.Direction.ASC);
        filterSortRequest.setSort(sort);
        filterSortRequest.setFilter(userIdsRequest);
        filterSortRequest.setPage(pagination);
        String json = new Gson().toJson(filterSortRequest);
        MvcResult mvcResult = mockMvc.perform(post("/users/get/filter/userBookDetails?=")
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void userChartApi() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        int year;
        year=2021;
        MvcResult mvcResult = mockMvc.perform(get("/users/chart?year="+year)
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataResponse dataResponse = new Gson().fromJson(result,new TypeToken<DataResponse>(){}.getType());
        Assertions.assertEquals("Ok", dataResponse.getStatus().getStatus(), dataResponse.getStatus().getDescription());
    }
    @Test
    void getUserListByPagination() throws Exception {
        UserModel userModel = dataSetHelper.getUserModel();
        JWTUser jwtUser = new JWTUser(userModel.getId(),Collections.singletonList(userModel.getRole().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        MvcResult mvcResult = mockMvc.perform(get("/users/getUserAll/filter")
                        .header(CustomHTTPHeaders.TOKEN.toString(),token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        ListResponse listResponse = new Gson().fromJson(result,new TypeToken<ListResponse>(){}.getType());
        Assertions.assertEquals("Ok", listResponse.getStatus().getStatus(), listResponse.getStatus().getDescription());
    }
}
