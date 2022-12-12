package com.example.authmoduls.service;

import com.example.authmoduls.ar.auth.decorator.LoginAddRequest;
import com.example.authmoduls.ar.auth.decorator.LoginFilter;
import com.example.authmoduls.ar.auth.decorator.LoginResponse;
import com.example.authmoduls.ar.auth.decorator.LoginSortBy;
import com.example.authmoduls.ar.auth.model.Gender;
import com.example.authmoduls.ar.auth.model.Login;
import com.example.authmoduls.ar.auth.repository.LoginRepository;
import com.example.authmoduls.ar.auth.service.LoginService;
import com.example.authmoduls.ar.auth.service.LoginServiceImpl;
/*import com.example.authmoduls.auth.rabbitmq.UserPublisher;*/
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
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
@AutoConfigureMockMvc
@Slf4j
class LoginServiceImplTest {

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
    /*private final UserPublisher userPublisher = mock(UserPublisher.class);*/
    private final LoginService loginService = new LoginServiceImpl(loginRepository,nullAwareBeanUtilsBean,jwtTokenUtil,passwordUtils,adminService,utils,notificationParser,modelMapper/*,userPublisher*/);

    private ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }
    @Test
    void testAddOrUpdate() throws InvocationTargetException, IllegalAccessException {
        //LoginAddRequest loginAddRequest, String id, Role role, Gender gender
        //given
        var loginAddRequest = LoginAddRequest.builder().firstName(fullName).email(email).build();
        var login = Login.builder().id(id).fullName(fullName).email(email).build();
        var loginResponse = LoginResponse.builder().id(id).fullName(fullName).email(email).build();

        when(loginRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(login));
        nullAwareBeanUtilsBean.copyProperties(login, loginAddRequest);

        doNothing().when(nullAwareBeanUtilsBean).copyProperties(loginResponse,login);

        //when
        loginService.addOrUpdateUsers(loginAddRequest,id,role,gender);
        //then
        Assertions.assertEquals(loginResponse,loginService.addOrUpdateUsers(loginAddRequest,id,role,gender));
    }

    @Test
    void testGetUser() throws InvocationTargetException, IllegalAccessException {
        //given
        var login = Login.builder().id(id).fullName(fullName).email(email).role(role).gender(gender).build();
        var loginResponse = LoginResponse.builder().id(id).email(email).fullName(fullName).role(role).gender(gender).build();
        when(loginRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(login));
        //when
        loginService.getUser(id);

        //then
        Assertions.assertEquals(loginResponse,loginService.getUser(id));
    }



    @Test
    void testDeleteUser(){
        //given
        var login = Login.builder().fullName(fullName).email(email).id(id).softDelete(true).build();
        when(loginRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(login));

        //When
        loginService.deleteUser(id);

        //then
        verify(loginRepository,times(1)).findByIdAndSoftDeleteIsFalse(id);
    }

    @Test
    void testGetAllUser() throws InvocationTargetException, IllegalAccessException {
        //given
        var login = List.of(Login.builder().id(id).fullName(fullName).email(email).role(role).gender(gender).build());
        var loginResponse = List.of(LoginResponse.builder().id(id).fullName(fullName).email(email).role(role).gender(gender).build());
        when(loginRepository.findAllBySoftDeleteFalse()).thenReturn(login);
        /*doNothing().when(nullAwareBeanUtilsBean).copyProperties(loginResponse,login);*/
        //when
        loginService.getAllUser();
        //then
        Assertions.assertEquals(loginResponse,loginService.getAllUser());
    }

    @Test
    void testGetAllUserByFilterAndSortAndPage(){
        //given
        //(LoginFilter loginFilter, FilterSortRequest.SortRequest<LoginSortBy> sort, PageRequest pagination);
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setGender(loginFilter.getGender());
        FilterSortRequest.SortRequest<LoginSortBy> sort = new FilterSortRequest.SortRequest<>();
        sort.setSortBy(LoginSortBy.EMAIL);
        sort.setOrderBy(Sort.Direction.ASC);
        Pagination pagination = new Pagination();
        pagination.setLimit(5);
        pagination.setPage(1);
        PageRequest pageRequest = PageRequest.of(pagination.getLimit(),pagination.getPage());
        var login = List.of(Login.builder().id(id).email(email).fullName(fullName).gender(gender).role(role).build());
        Page<Login> page = new PageImpl<>(login);

        when(loginRepository.findAllUserByFilterAndSortAndPage(loginFilter, sort, pageRequest)).thenReturn(page);
        //when
        loginService.getAllUserByFilterAndSortAndPage(loginFilter,sort,pageRequest);

        //then
        Assertions.assertEquals(page,loginService.getAllUserByFilterAndSortAndPage(loginFilter,sort,pageRequest));

    }

    @Test
    void testGetToken() throws InvocationTargetException, IllegalAccessException {
        //given
        List<String> roles = new ArrayList<>();
        roles.add(Role.ANONYMOUS.toString());
        var jwtUser = JWTUser.builder().id(id).role(roles).build();
        String token = jwtTokenUtil.generateToken(jwtUser);
        var login = Login.builder().id(id).email(email).role(role).build();
        var loginResponse = LoginResponse.builder().id(id).email(email).role(role).build();
        when(jwtTokenUtil.generateToken(jwtUser)).thenReturn(token);
        when(loginRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(login));
        /*doNothing().when(nullAwareBeanUtilsBean).copyProperties(loginResponse, login);*/
        //when
        loginService.getToken(id);

        //then
        Assertions.assertEquals(loginResponse,loginService.getToken(id));
    }

    @Test
    void testUserLogin() throws InvocationTargetException, IllegalAccessException, NoSuchAlgorithmException {
        //given
        var login = Login.builder().id(id).email(email).role(role).gender(gender).passWord(PasswordUtils.encryptPassword(passWord)).confirmPassword(PasswordUtils.encryptPassword(passWord)).build();
        var adminConfiguration = AdminConfiguration.builder().from(from).id(id).nameRegex(nameRegex).passwordRegex(passwordRegex).host(host).port(port).build();
        var emailModel = EmailModel.builder().to(to).cc(Collections.singleton(cc)).message(message).subject(subject).build();
        when(loginRepository.findByEmailAndSoftDeleteIsFalse(email)).thenReturn(Optional.ofNullable(login));
        when(adminService.getConfigurationDetails()).thenReturn(adminConfiguration);
        when(passwordUtils.isPasswordAuthenticated(PasswordUtils.encryptPassword(passWord), PasswordUtils.encryptPassword(passWord), PasswordEncryptionType.BCRYPT)).thenReturn(true);

        //when
        loginService.userLogin(email,PasswordUtils.encryptPassword(passWord));

        //then
        verify(loginRepository,times(1)).findByEmailAndSoftDeleteIsFalse(email);
    }

    @Test
    void testGetEncryptPassword() throws InvocationTargetException, IllegalAccessException {

        //given
        var login = Login.builder().id(id).fullName(fullName).email(email).passWord(PasswordUtils.encryptPassword(passWord)).role(role).build();
        /*var loginResponse = LoginResponse.builder().id(id).fullName(fullName).email(email).passWord(PasswordUtils.encryptPassword(passWord)).role(role).build();*/
        when(loginRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(login));
        //when
        loginService.getEncryptPassword(id);

        //then
        Assertions.assertEquals(PasswordUtils.encryptPassword(passWord),loginService.getEncryptPassword(id));
    }

    @Test
    void testGetValidityOfToken() throws InvocationTargetException, IllegalAccessException {

        //given
        List<String> roles = new ArrayList<>();
        roles.add(Role.ANONYMOUS.toString());
        var jwtUser = JWTUser.builder().id(id).role(roles).build();
        String token = jwtTokenUtil.generateToken(jwtUser);
        var login = Login.builder().id(id).role(role).passWord(passWord).token(token).build();
        var loginResponse = LoginResponse.builder().role(role).passWord(passWord).token(token).build();
        when(loginRepository.existsByIdAndSoftDeleteFalse(id)).thenReturn(true);
        when(loginRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(login));
        when(jwtTokenUtil.validateToken(token, jwtUser)).thenReturn(true);

        //when
        loginService.getValidityOfToken(token);

        //then
        Assertions.assertEquals(loginResponse,loginService.getValidityOfToken(token));

    }

    @Test
    void testLogOut(){
        //given
        var login = Login.builder().id(id).login(false).build();
        when(loginRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(login));
        //when
        loginService.logOut(id);
        //then
        verify(loginRepository,times(1)).findByIdAndSoftDeleteIsFalse(id);
    }

}
