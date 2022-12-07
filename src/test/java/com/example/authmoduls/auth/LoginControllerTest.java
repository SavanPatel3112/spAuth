package com.example.authmoduls.auth;

import com.example.authmoduls.ar.auth.decorator.LoginAddRequest;
import com.example.authmoduls.ar.auth.decorator.LoginFilter;
import com.example.authmoduls.ar.auth.decorator.LoginResponse;
import com.example.authmoduls.ar.auth.decorator.LoginSortBy;
import com.example.authmoduls.ar.auth.model.Gender;
import com.example.authmoduls.ar.auth.model.Login;
import com.example.authmoduls.ar.auth.repository.LoginRepository;
import com.example.authmoduls.ar.auth.service.LoginService;
import com.example.authmoduls.ar.auth.service.LoginServiceImpl;
import com.example.authmoduls.auth.controller.LoginController;
import com.example.authmoduls.auth.rabbitmq.UserPublisher;
import com.example.authmoduls.common.constant.MessageConstant;
import com.example.authmoduls.common.constant.ResponseConstant;
/*import com.example.authmoduls.common.decorator.AbstractContainerTest;*/
import com.example.authmoduls.common.decorator.*;
import com.example.authmoduls.common.enums.PasswordEncryptionType;
import com.example.authmoduls.common.enums.Role;
import com.example.authmoduls.common.model.AdminConfiguration;
import com.example.authmoduls.common.model.EmailModel;
import com.example.authmoduls.common.model.JWTUser;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

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
    private static final String title = "All Data";
    private static final String newPassword = "Aa@123456";
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
    private final LoginRepository loginRepository = mock(LoginRepository.class);
    private final NullAwareBeanUtilsBean nullAwareBeanUtilsBean = mock(NullAwareBeanUtilsBean.class);
    private final JwtTokenUtil jwtTokenUtil = mock(JwtTokenUtil.class);
    private final PasswordUtils passwordUtils = spy(PasswordUtils.class);
    private final AdminConfigurationService adminService = mock(AdminConfigurationService.class);
    private final Utils utils = spy(Utils.class);
    private final NotificationParser notificationParser = mock(NotificationParser.class);
    private final ModelMapper modelMapper = getModelMapper();
    private final UserPublisher userPublisher = mock(UserPublisher.class);

    private final LoginService loginService = new LoginServiceImpl(loginRepository,nullAwareBeanUtilsBean,jwtTokenUtil,passwordUtils,adminService,utils,notificationParser,modelMapper,userPublisher);

    private ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    @Test
    void testAddOrUpdateUser() throws InvocationTargetException, IllegalAccessException {
        //given
        var loginResponse = LoginResponse.builder().role(role).id(id).fullName(fullName).softDelete(false).email(email).build();
        var loginAddRequest = LoginAddRequest.builder().fullName(fullName).email(email).build();
        var adminConfiguration = AdminConfiguration.builder().port(port).host(host).from(from).passwordRegex(passwordRegex).emailRegex(emailRegex).nameRegex(nameRegex).build();

        Login login = new Login();
        login.setId(id);
        login.setSoftDelete(false);
        loginRepository.insert(login);
        when(loginRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.of(login));
        when(adminService.getConfigurationDetails()).thenReturn(adminConfiguration);
        nullAwareBeanUtilsBean.copyProperties(loginResponse,login);
        //when

        DataResponse<LoginResponse> dataResponse = loginController.addOrUpdateUser(loginAddRequest,id,role,gender);

        //then
        LoginResponse responseData = dataResponse.getData();
        Assert.assertEquals(loginResponse, responseData);
    }

    @Test
    void testGetUser() throws InvocationTargetException, IllegalAccessException {
        //given
        var loginResponse = LoginResponse.builder().role(role).id(id).fullName(fullName).softDelete(false).email(email).build();
        Login login = new Login();
        login.setId(id);
        login.setSoftDelete(false);
        loginRepository.insert(login);
        when(loginRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.of(login));

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
        when(loginRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.of(login));

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
        var loginResponse = List.of(LoginResponse.builder().id(id).role(role).fullName(fullName).softDelete(false).email(email).build());
        when(loginRepository.findAllBySoftDeleteFalse()).thenReturn(Collections.singletonList(login));

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
        var login = List.of(Login.builder().id(id).role(role).fullName(fullName).softDelete(false).email(email).build());
        Page<Login> page = new PageImpl<>(login);
        when(loginRepository.findAllUserByFilterAndSortAndPage(loginFilter, sort, pageRequest)).thenReturn(page);

        //when
        PageResponse<Login> pageResponse = loginController.getAllUserByPagination(filterSortRequest);

        //then
        Page<Login> logins = pageResponse.getData();
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
        var jwtUser = JWTUser.builder().id(id).role(roles).build();
        String token = jwtTokenUtil.generateToken(jwtUser);
        var loginResponse = LoginResponse.builder().id(id).role(role).softDelete(false).token(token).build();
        when(loginRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.of(login));

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
        var adminConfiguration = AdminConfiguration.builder().port(port).host(host).from(from).passwordRegex(passwordRegex)
                .emailRegex(emailRegex).nameRegex(nameRegex).smtpAuth(true).starttls(true).build();
        var emailModel = EmailModel.builder().to(to).cc(Collections.singleton(cc)).message(message).subject(subject).build();
        when(loginRepository.findByEmailAndSoftDeleteIsFalse(email)).thenReturn(Optional.of(login));
        when(adminService.getConfigurationDetails()).thenReturn(adminConfiguration);
        when(passwordUtils.isPasswordAuthenticated(PasswordUtils.encryptPassword(passWord), PasswordUtils.encryptPassword(passWord), PasswordEncryptionType.BCRYPT)).thenReturn(true);
        doNothing().when(utils).sendEmailNow(emailModel);
        when(adminService.getConfigurationDetails()).thenReturn(adminConfiguration);
        //when
        DataResponse<Object> userLogin = loginController.userLogin(email , passWord);
        //then
        Response responseData = userLogin.getStatus();
        Assert.assertEquals(ResponseConstant.OK, responseData.getStatus());
    }

    @Test
    void testGetEncryptPassword() throws InvocationTargetException, IllegalAccessException {
        //given
        Login login = new Login();
        login.setId(id);
        login.setSoftDelete(false);
        loginRepository.insert(login);
        when(loginRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.of(login));

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
        var jwtUser = JWTUser.builder().id(id).role(roles).build();
        String token = jwtTokenUtil.generateToken(jwtUser);
        var loginResponse = LoginResponse.builder().id(id).token(token).softDelete(true).build();
        when(jwtTokenUtil.getUserIdFromToken(token)).thenReturn(id);
        when(loginRepository.existsByIdAndSoftDeleteFalse(id)).thenReturn(true);
        when(loginRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.of(login));
        when(jwtTokenUtil.validateToken(token, jwtUser)).thenReturn(true);

        //when
        TokenResponse<LoginResponse> tokenResponse = loginController.getValidityOfToken(token);

        //then
        LoginResponse loginResponse1 = tokenResponse.getData();
        Assertions.assertEquals(loginResponse,loginResponse1);
    }

    @Test
    void testGetIdFromToken(){
        //given
        List<String> roles = new ArrayList<>();
        roles.add(Role.ADMIN.toString());
        var jwtUser = JWTUser.builder().id(id).role(roles).build();
        String token = jwtTokenUtil.generateToken(jwtUser);
        when(loginRepository.existsByIdAndSoftDeleteFalse(id)).thenReturn(true);
        when(jwtTokenUtil.getUserIdFromToken(token)).thenReturn(token);

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
        when(loginRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.of(login));

        //when
        DataResponse<Object> dataResponse = loginController.logOut(id);

        //then
        Response response = dataResponse.getStatus();
        Assertions.assertEquals(ResponseConstant.OK,response.getStatus());


    }

}
