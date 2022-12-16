package com.example.authmoduls.auth;

/*import com.example.authmoduls.AbstractContainerTest;*/
import com.example.authmoduls.ar.auth.decorator.*;
import com.example.authmoduls.ar.auth.model.Gender;
import com.example.authmoduls.ar.auth.model.Login;
import com.example.authmoduls.ar.auth.repository.LoginRepository;
import com.example.authmoduls.auth.controller.LoginController;
import com.example.authmoduls.auth.model.Accesss;
import com.example.authmoduls.common.constant.ResponseConstant;
import com.example.authmoduls.common.decorator.*;
import com.example.authmoduls.common.enums.Role;
import com.example.authmoduls.common.model.AdminConfiguration;
import com.example.authmoduls.common.model.JWTUser;
import com.example.authmoduls.common.repository.AdminRepository;
import com.example.authmoduls.common.service.AdminConfigurationService;
import com.example.authmoduls.common.utils.JwtTokenUtil;
import com.example.authmoduls.common.utils.PasswordUtils;
import com.example.authmoduls.common.utils.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.testng.Assert;

import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Deprecated
class LoginControllerTest  {

    @Autowired
    LoginController loginController;

    private static final String id = "62ea7609e4eb6b6d0bf0aebc";
    private static final String userName = "sp3112";
    private static final String firstName = "Savan";
    private static final String middleName = "Kiritbhai";
    private static final String lastName = "Patel";
    private static final String fullName = "Savan Kiritbhai Patel";
    private static final String email = "savan9045@gmail.com";
    private static final Role role = Role.ANONYMOUS;
    private static final Gender gender = Gender.FEMALE;
    private static final String passWord = "Aa@123456";
    private static final String confirmPassword = "Aa@123456";
    private static final String host = "smtp.office365.com";
    private static final String port = "587";
    private static final String otp = "123456";
    private static final String from = "savan.p@techroversolutions.com";
    private static final String message = "successfully";
    private static final String to = "savan.p@techroversolutions.com";
    private static final String cc = "savan.p@techroversolutions.com";
    private static final String subject = "user";
    private static final String nameRegex = "^[0-9#$@!%&*?.-_=]{1,15}$";
    private static final String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,15}$";
    private static final String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String requiredEmailItems = "@";

    @Autowired
    LoginRepository loginRepository;
    @Autowired
    NullAwareBeanUtilsBean nullAwareBeanUtilsBean;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    PasswordUtils passwordUtils;
    @Autowired
    AdminConfigurationService adminService;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    Utils utils;

    private ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }



    @Test
    void testAddOrUpdateUser() throws InvocationTargetException, IllegalAccessException {
        //given
        var loginResponse = LoginResponse.builder().accesss(Accesss.ADMIN).id(id).fullName(fullName).softDelete(false).email(email).build();
        var loginAddRequest = LoginAddRequest.builder().email(email).build();
        var adminConfiguration = AdminConfiguration.builder().port(port).host(host).from(from).passwordRegex(passwordRegex).emailRegex(emailRegex).nameRegex(nameRegex).build();
        
        Login login = new Login();
        login.setId(id);
        login.setSoftDelete(false);
        loginRepository.insert(login);
        adminRepository.save(adminConfiguration);
        //when
        DataResponse<LoginResponse> dataResponse = loginController.addOrUpdateUser(loginAddRequest,id,Accesss.ADMIN,gender);

        //then
        LoginResponse responseData = dataResponse.getData();
        Assert.assertEquals(loginResponse, responseData);
    }

    @Test
    void testGetUser() throws InvocationTargetException, IllegalAccessException {
        //given
        var loginResponse = LoginResponse.builder().accesss(Accesss.ADMIN).id(id).fullName(fullName).softDelete(false).email(email).build();
        Login login = new Login();
        login.setId(id);
        login.setSoftDelete(false);
        loginRepository.insert(login);

        //when
        DataResponse<LoginResponse> dataResponse = loginController.getUser(id);

        //then
        LoginResponse response = dataResponse.getData();
        Assertions.assertEquals(loginResponse,response);

    }

    @Test
    void testDeleteUser(){
        //given
        Login login = new Login();
        login.setId(id);
        login.setSoftDelete(false);
        loginRepository.insert(login);

        //when
        DataResponse<LoginResponse> dataResponse = loginController.deleteUser(id);

        //then
        Response response = dataResponse.getStatus();
        Assertions.assertEquals(ResponseConstant.OK,response.getStatus());

    }

    @Test
    void testGetAllUser() throws InvocationTargetException, IllegalAccessException {
        //given
        Login login = new Login();
        login.setSoftDelete(false);
        loginRepository.insert(login);
        var loginResponse = List.of(LoginResponse.builder().id(id).accesss(Accesss.ADMIN).fullName(fullName).softDelete(false).email(email).build());

        //when
        ListResponse<LoginResponse> listResponse = loginController.getAllUser();

        //then
        List<LoginResponse> loginResponse1 = listResponse.getData();
        Assertions.assertEquals(loginResponse1,loginResponse);
    }

    @Test
    void testGetAllUserByPagination(){
        //given
        FilterSortRequest<LoginFilter,LoginSortBy> filterSortRequest = new FilterSortRequest<>();
        LoginFilter loginFilter = filterSortRequest.getFilter();
        loginFilter.setGender(loginFilter.getGender());
        FilterSortRequest.SortRequest<LoginSortBy> sort = new FilterSortRequest.SortRequest<>();
        sort.setSortBy(LoginSortBy.EMAIL);
        sort.setOrderBy(Sort.Direction.ASC);
        Pagination pagination = new Pagination();
        pagination.setLimit(5);
        pagination.setPage(1);
        PageRequest pageRequest = PageRequest.of(pagination.getPage(),pagination.getLimit());
        var login = List.of(Login.builder().id(id).accesss(Accesss.ADMIN).fullName(fullName).softDelete(false).email(email).build());
        Page<Login> page = new PageImpl<>(login);
        //when
        PageResponse<LoginResponse> pageResponse = loginController.getAllUserByPagination(filterSortRequest);

        //then
        Page<LoginResponse> logins = pageResponse.getData();
        Assertions.assertEquals(page,logins);

    }

    @Test
    void testGetToken (){
        //given
        Login login = new Login();
        login.setId(id);
        login.setSoftDelete(false);
        loginRepository.insert(login);
        List<String> roles = new ArrayList<>();
        roles.add(Role.ANONYMOUS.toString());
        var jwtUser = JWTUser.builder().id(id).accesss(roles).build();
        String token = jwtTokenUtil.generateToken(jwtUser);
        var loginResponse = LoginResponse.builder().id(id).accesss(Accesss.ADMIN).softDelete(false).token(token).build();

        //when
        TokenResponse<LoginResponse> tokenResponse = loginController.getToken(id);

        //then
        LoginResponse loginResponse1 = tokenResponse.getData();
        Assertions.assertEquals(loginResponse,loginResponse1);

    }


    @Test
    void testUserLogin() throws InvocationTargetException, IllegalAccessException, NoSuchAlgorithmException {
        //given
        Login login = new Login();
        login.setEmail(email);
        login.setSoftDelete(false);
        loginRepository.insert(login);
        AdminConfiguration adminConfiguration = new AdminConfiguration();
        adminConfiguration.setNameRegex(nameRegex);
        adminConfiguration.setEmailRegex(emailRegex);
        adminConfiguration.setFrom(from);
        adminConfiguration.setHost(host);
        adminConfiguration.setPort(port);
        adminRepository.save(adminConfiguration);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(PasswordUtils.encryptPassword(passWord));

        //when
        /*DataResponse<Object> userLogin = loginController.userLogin(loginRequest);*/
        //then
        /*Response responseData = userLogin.getStatus();*/
        /*Assert.assertEquals(ResponseConstant.OK, responseData.getStatus());*/
    }

    @Test
    void testGetEncryptPassword() throws InvocationTargetException, IllegalAccessException {
        //given
        Login login = new Login();
        login.setId(id);
        login.setSoftDelete(false);
        loginRepository.insert(login);

        //when
        DataResponse<String> dataResponse = loginController.getEncryptPassword(id);

        //then
        String string = dataResponse.getData();
        Assertions.assertEquals(PasswordUtils.encryptPassword(passWord),string);

    }

    @Test
    void testGetValidityOfToken(){
        //given
        Login login = new Login();
        login.setId(id);
        login.setSoftDelete(false);
        loginRepository.insert(login);
        List<String> roles = new ArrayList<>();
        roles.add(Role.ADMIN.toString());
        var jwtUser = JWTUser.builder().id(id).accesss(roles).build();
        String token = jwtTokenUtil.generateToken(jwtUser);
        var loginResponse = LoginResponse.builder().id(id).token(token).softDelete(true).build();

        //when
        TokenResponse<LoginResponse> tokenResponse = loginController.getValidityOfToken(token);

        //then
        LoginResponse loginResponse1 = tokenResponse.getData();
        Assertions.assertEquals(loginResponse,loginResponse1);
    }

    @Test
    void testGetIdFromToken(){
        //given
        Login login = new Login();
        login.setId(id);
        login.setSoftDelete(false);
        loginRepository.insert(login);
        List<String> roles = new ArrayList<>();
        roles.add(Role.ADMIN.toString());
        var jwtUser = JWTUser.builder().id(id).accesss(roles).build();
        String token = jwtTokenUtil.generateToken(jwtUser);

        //when
        TokenResponse<String> tokenResponse = loginController.getIdFromToken(token);

        //then
        String string = tokenResponse.getData();
        Assertions.assertEquals(token,string);

    }

    @Test
    void testLogOut(){
        //given
        Login login = new Login();
        login.setId(id);
        login.setSoftDelete(false);
        loginRepository.insert(login);

        //when
        DataResponse<Object> dataResponse = loginController.logOut(id);

        //then
        Response response = dataResponse.getStatus();
        Assertions.assertEquals(ResponseConstant.OK,response.getStatus());

    }

}
