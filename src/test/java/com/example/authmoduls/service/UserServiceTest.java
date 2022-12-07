package com.example.authmoduls.service;


import com.example.authmoduls.auth.decorator.*;
import com.example.authmoduls.auth.enums.UserSortBy;
import com.example.authmoduls.auth.enums.UserStatus;
import com.example.authmoduls.auth.model.Address;
import com.example.authmoduls.auth.model.UserModel;
import com.example.authmoduls.auth.service.userService.UserService;
import com.example.authmoduls.common.decorator.FilterSortRequest;
import com.example.authmoduls.common.decorator.PageResponse;
import com.example.authmoduls.common.decorator.Pagination;
import com.example.authmoduls.common.decorator.UserImportVerifyRequest;
import com.example.authmoduls.common.enums.Role;
import com.example.authmoduls.common.model.JWTUser;
import com.example.authmoduls.common.utils.JwtTokenUtil;
import com.example.authmoduls.common.utils.PasswordUtils;
import com.example.authmoduls.helper.DataSetHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Slf4j
public class UserServiceTest {
    @Autowired
    DataSetHelper dataSetHelper;
    @Autowired
    UserService userService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @BeforeEach
    public void getDataSetHelper() {
        dataSetHelper.cleanUp();
        dataSetHelper.init();
    }
    @Test
    void getUser(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            userService.getUser(userModel.getId());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void getUsers(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            userService.getUser(userModel.getUserName());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void getDeleteUser(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            userService.deleteUser(userModel.getId());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void getDeleteUsers(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            userService.deleteUser(userModel.getEmail());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void getAllUser(){
        try {
            Assertions.assertEquals(userService.getAllUser(), new ArrayList<UserResponse>());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void addOrUpdateUser(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            UserAddRequest userAddRequest = new UserAddRequest();
            userAddRequest.setUserName("pk1402");
            userAddRequest.setEmail("pk@102.com");
            userAddRequest.setFirstName("Pinkesh");
            userAddRequest.setMiddleName("Kiritbhai");
            userAddRequest.setLastName("Patel");
            userAddRequest.setPassword("Pk@1402");
            userAddRequest.setAddress((List<Address>) new Address("f-7 neelDip flat","guruKulRoad","memNagar","ahmedabad","gujarat","380062"));
            userService.addOrUpdateUser(userAddRequest, userModel.getId(), userModel.getRole());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void addOrUpdateUsers(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            UserAddRequest userAddRequest = new UserAddRequest();
            userAddRequest.setUserName("pk1402");
            userAddRequest.setEmail("pk@102.com");
            userAddRequest.setFirstName("Pinkesh");
            userAddRequest.setMiddleName("Kiritbhai");
            userAddRequest.setLastName("Patel");
            userAddRequest.setPassword("Pk@1402");
            userAddRequest.setAddress((List<Address>) new Address("f-7 neelDip flat","guruKulRoad","memNagar","ahmedabad","gujarat","380062"));
            Assertions.assertEquals(userService.addOrUpdateUser(userAddRequest, userModel.getUserName(), userModel.getRole()),new UserResponse());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void getAllUserByPagination(){
        try {
            UserFilter userFilter = new UserFilter();
            userFilter.setRole(userFilter.getRole());
            FilterSortRequest.SortRequest<UserSortBy> sort = new FilterSortRequest.SortRequest<>();
            sort.setSortBy(UserSortBy.EMAIL);
            sort.setOrderBy(Sort.Direction.ASC);
            Pagination pagination = new Pagination();
            pagination.setLimit(1);
            pagination.setPage(1);
            PageRequest pageRequest = PageRequest.of(pagination.getLimit(),pagination.getPage());
            Assertions.assertEquals(userService.getAllUserByFilterAndSortAndPage(userFilter, sort, pageRequest), new PageResponse<UserModel>());

        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void getAllUserByPaginations(){
        try {
            UserFilter userFilter = new UserFilter();
            userFilter.setRole(userFilter.getRole());
            FilterSortRequest.SortRequest<UserSortBy> sort = new FilterSortRequest.SortRequest<>();
            sort.setSortBy(UserSortBy.EMAIL);
            sort.setOrderBy(Sort.Direction.ASC);
            Pagination pagination = new Pagination();
            pagination.setLimit(0);
            pagination.setPage(0);
            PageRequest pageRequest = PageRequest.of(pagination.getLimit(),pagination.getPage());
            Assertions.assertEquals(userService.getAllUserByFilterAndSortAndPage(userFilter, sort, pageRequest), new PageResponse<UserModel>());

        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void getToken(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            Assertions.assertEquals(userService.getToken(userModel.getId()),new UserResponse());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void getTokens(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            Assertions.assertEquals(userService.getToken(userModel.getUserName()),new UserResponse());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void getEncryptPassword(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            Assertions.assertEquals(userService.getEncryptPassword(userModel.getId()),PasswordUtils.encryptPassword(""));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void getEncryptPasswords(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            Assertions.assertEquals(userService.getEncryptPassword(userModel.getId()), PasswordUtils.encryptPassword(userModel.getPassWord()));
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void checkUserAuthentication(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            userModel.setPassWord("sp3112");
            userService.checkUserAuthentication(userModel.getEmail(),userModel.getPassWord());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void checkUserAuthentications(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            userModel.setPassWord("sp3112");
            userService.checkUserAuthentication(userModel.getUserName(),userModel.getPassWord());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void getIdFromToken(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            JWTUser jwtUser = new JWTUser(userModel.getId(), Collections.singletonList(userModel.getRole().toString()));
            String token = jwtTokenUtil.generateToken(jwtUser);
            Assertions.assertEquals(userService.getIdFromToken(token),userModel.getId());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void getIdFromTokens(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            JWTUser jwtUser = new JWTUser(userModel.getId(), Collections.singletonList(userModel.getRole().toString()));
            String token = jwtTokenUtil.generateToken(jwtUser);
            Assertions.assertEquals(userService.getIdFromToken(token),token);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void getValidityOfToken(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            JWTUser jwtUser = new JWTUser(userModel.getId(), Collections.singletonList(userModel.getRole().toString()));
            String token = jwtTokenUtil.generateToken(jwtUser);
            /*Assertions.assertEquals(*/userService.getValidityOfToken(token)/*,token)*/;
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void getValidityOfTokens(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            JWTUser jwtUser = new JWTUser(userModel.getId(), Collections.singletonList(userModel.getRole().toString()));
            String token = jwtTokenUtil.generateToken(jwtUser);
            /*Assertions.assertEquals(*/userService.getValidityOfToken("hhas423jbsadfhh5h25hncn,4234bbnr23j34bnk,234nj")/*,token)*/;
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void userLogin(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            userModel.setEmail("savan9045@gmail.com");
            userModel.setPassWord("sp3112");
            userService.userLogin(userModel.getEmail(),userModel.getPassWord());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void userLogins(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            userModel.setPassWord("sp3112");
            userService.userLogin(userModel.getUserName(),userModel.getPassWord());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void getOtp(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            userService.getOtp(userModel.getOtp(),userModel.getId());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void getOtps(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            userService.getOtp(userModel.getOtp(),userModel.getEmail());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void forgotPassword(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            userService.forgotPassword(userModel.getEmail());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void forgotPasswords(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            userService.forgotPassword(userModel.getId());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void setPassword(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            userModel.setPassWord("sp3112");
            userModel.setConfirmPassWord("sp3112");
            userService.setPassword(userModel.getPassWord(), userModel.getConfirmPassWord(), userModel.getId());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void setPasswords(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            userModel.setPassWord("sp3112");
            userModel.setConfirmPassWord("Sp3112");
            userService.setPassword(userModel.getPassWord(), userModel.getConfirmPassWord(), userModel.getId());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void logOut(String id){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            userService.logOut(id);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void logOuts(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            userService.logOut(userModel.getId());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void getUserByRole(){
        try {
            UserFilter userFilter = new UserFilter();
            userFilter.setRole(Role.ADMIN);
            userService.getUserByRole(userFilter);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void addResult(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            Result result = new Result();
            result.setSemester(10);
            result.setSpi(9.6);
            result.setYear(2021);
            userService.addResult(result,userModel.getId());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void getUserResult(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            UserDetail userDetail = new UserDetail();
            userDetail.setUserIds(Collections.singleton(userModel.getId()));
            userDetail.setSemester(1);
            userService.getUserResult(userDetail);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void getUserResultBySemester(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            UserResult userResult = new UserResult();
            userResult.setUserIds(Collections.singleton(userModel.getId()));
            userResult.setSemester(Collections.singleton(3));
            userService.getUserResultBySemester(userResult);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void getUserResultByMinMaxMark(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            UserIdsRequest userIdsRequest = new UserIdsRequest();
            userIdsRequest.setUserId(Collections.singleton(userModel.getId()));
            userService.getUserResultByMinMaxSem(userIdsRequest);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void deleteUserResult(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            userService.deleteUserResult(userModel.getId(),3);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void getUserResultStatusByPagination(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            UserIdsRequest userIdsRequest =new UserIdsRequest();
            FilterSortRequest.SortRequest<UserSortBy> userSortBySortRequest = new FilterSortRequest.SortRequest<>();
            Pagination pagination = new Pagination();
            pagination.setPage(1);
            pagination.setLimit(1);
            userSortBySortRequest.setSortBy(UserSortBy.EMAIL);
            userSortBySortRequest.setOrderBy(Sort.Direction.ASC);
            userIdsRequest.setUserId(Collections.singleton(userModel.getId()));
            PageRequest pageRequest = PageRequest.of(pagination.getLimit(),pagination.getPage());
            userService.getUserResultStatusByPagination(userIdsRequest,userSortBySortRequest,pageRequest);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void getUserByExcel(){
        try {
            UserFilter userFilter =new UserFilter();
            FilterSortRequest.SortRequest<UserSortBy> userSortBySortRequest = new FilterSortRequest.SortRequest<>();
            Pagination pagination = new Pagination();
            pagination.setPage(1);
            pagination.setLimit(1);
            userSortBySortRequest.setSortBy(UserSortBy.EMAIL);
            userSortBySortRequest.setOrderBy(Sort.Direction.ASC);
            userFilter.setRole(Role.ADMIN);
            PageRequest pageRequest = PageRequest.of(pagination.getLimit(),pagination.getPage());
            userService.getUserByExcel(userFilter,userSortBySortRequest,pageRequest);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void resultDetailByEmail(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            userService.resultDetailByEmail(userModel.getId());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void userUpdate(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            UserAddRequest userAddRequest = new UserAddRequest();
            userAddRequest.setUserName("pk1402");
            userAddRequest.setEmail("pk@102.com");
            userAddRequest.setFirstName("Pinkesh");
            userAddRequest.setMiddleName("Kiritbhai");
            userAddRequest.setLastName("Patel");
            userAddRequest.setPassword("Pk@1402");
            userAddRequest.setAddress((List<Address>) new Address("f-7 neelDip flat","guruKulRoad","memNagar","ahmedabad","gujarat","380062"));
            userService.userUpdate(userModel.getId(),userModel.getRole(),userAddRequest);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void userDelete(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            userService.userDelete(userModel.getId(),userModel.getRole());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void uploadFile(){
        try {
            File file = new File("E:\\java\\randomdata.xlsx");
            FileInputStream inputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(),file.getName(), MediaType.MULTIPART_FORM_DATA.toString(), IOUtils.toByteArray(inputStream));
            userService.uploadFile(multipartFile);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void importUsers(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            File file = new File("E:\\java\\randomdata.xlsx");
            FileInputStream inputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(),file.getName(), MediaType.MULTIPART_FORM_DATA.toString(), IOUtils.toByteArray(inputStream));
            userService.importUsers(multipartFile, userModel.getId());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void importUsersVerify(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            UserImportVerifyRequest userImportVerifyRequest = new UserImportVerifyRequest();
            userImportVerifyRequest.setId(Collections.singleton(userModel.getId()).toString());
            Map<String,String> map = null;
            /*userImportVerifyRequest.setMapping(map.put("firstName","firstName"));*/
            userService.importUsersVerify(userImportVerifyRequest);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void importDataInUser(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            UserIdsRequest userIdsRequest = new UserIdsRequest();
            userIdsRequest.setUserId(Collections.singleton(userModel.getId()));
            userService.importDataInUser(userIdsRequest);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void deleteUserInXls(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            userService.deleteUserInXls(userModel.getId());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void getUserPassword(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            userService.getUserPassword(userModel.getUserName(), userModel.getConfirmPassWord(), userModel.getPassWord());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void getUserPasswords(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            userService.getUserPassword(userModel.getId(), userModel.getConfirmPassWord(), userModel.getPassWord());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void sendMailToInvitedUser(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            userModel.setUserStatus(UserStatus.ACTIVE);
            userService.sendMailToInvitedUser(userModel.getUserStatus());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void userChartApi(){
        try {
            userService.userChartApi(2021);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void getUserBookDetailsByPagination(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            FilterSortRequest.SortRequest<UserDataSortBy> userIdsRequestSortRequest = new FilterSortRequest.SortRequest<>();
            userIdsRequestSortRequest.setSortBy(UserDataSortBy.FULL_NAME);
            userIdsRequestSortRequest.setOrderBy(Sort.Direction.ASC);
            UserIdsRequest userIdsRequest = new UserIdsRequest();
            userIdsRequest.setUserId(Collections.singleton(userModel.getId()));
            Pagination pagination = new Pagination();
            pagination.setLimit(10);
            pagination.setPage(1);
            PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getLimit());
            userService.getUserBookDetailsByPagination(userIdsRequest,userIdsRequestSortRequest,pageRequest);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void getUserListByPagination(){
        try {
            userService.getUserListByPagination();
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
}
