package com.example.authmoduls.service;

import com.example.authmoduls.ar.auth.decorator.*;
import com.example.authmoduls.ar.auth.model.Gender;
import com.example.authmoduls.ar.auth.model.Login;
import com.example.authmoduls.ar.auth.repository.LoginRepository;
import com.example.authmoduls.ar.auth.repository.ShoppingListLogRepository;
import com.example.authmoduls.ar.auth.service.LoginServiceImpl;
/*import com.example.authmoduls.auth.rabbitmq.UserPublisher;*/
import com.example.authmoduls.ar.auth.service.RecipeService;
import com.example.authmoduls.auth.model.Accesss;
import com.example.authmoduls.common.decorator.*;
import com.example.authmoduls.common.enums.PasswordEncryptionType;
import com.example.authmoduls.common.enums.Role;
import com.example.authmoduls.common.model.JWTUser;
import com.example.authmoduls.common.service.AdminConfigurationService;
import com.example.authmoduls.common.utils.JwtTokenUtil;
import com.example.authmoduls.common.utils.PasswordUtils;
import com.example.authmoduls.common.utils.Utils;
import com.example.authmoduls.helper.LoginServiceTestDataGenerator;
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
import org.testcontainers.shaded.com.google.common.annotations.VisibleForTesting;

import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
@AutoConfigureMockMvc
@Slf4j
class LoginServiceImplTest {

    private static final String id = "6398478a1311382b51c49584";
    private static final String recipeId = "639b03d1665fa56d3ec3ca95";
    private static final String loginId = "6398478a1311382b51c49584";
    private static final String email = "savan9045@gmail.com";
    private static final Accesss accesss = Accesss.USER;
    private static final Gender gender = Gender.FEMALE;
    private static final String passWord = ("$2a$12$WF3kZGAv0b0W/B/mCwc4h.2XcMJom/uKwTtB9fGLMyP/s8/t3Sihu");

    private final LoginRepository loginRepository = mock(LoginRepository.class);
    private final NullAwareBeanUtilsBean nullAwareBeanUtilsBean = mock(NullAwareBeanUtilsBean.class);
    private final JwtTokenUtil jwtTokenUtil = mock(JwtTokenUtil.class);
    private final AdminConfigurationService adminService = mock(AdminConfigurationService.class);
    private final Utils utils = spy(Utils.class);
    private final ModelMapper modelMapper = getModelMapper();
    /*private final RecipeService recipeService = mock(RecipeService.class);*/
    private final ShoppingListLogRepository listLogRepository = mock(ShoppingListLogRepository.class);
    private final LoginServiceImpl loginService = new LoginServiceImpl(loginRepository,nullAwareBeanUtilsBean,jwtTokenUtil,adminService,utils, modelMapper,listLogRepository/*, *//*recipeService*/);

    private ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    @Test
    void testAddOrUpdate() throws InvocationTargetException, IllegalAccessException, NoSuchAlgorithmException {
        //given
        var loginAddRequest = LoginServiceTestDataGenerator.loginAddRequest();
        var login = LoginServiceTestDataGenerator.login();
        var loginResponse=LoginServiceTestDataGenerator.loginResponse();

        when(loginRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(login));

        //when
        loginService.addOrUpdateUsers(loginAddRequest,id, Accesss.USER,gender);

        //then
        Assertions.assertEquals(loginResponse,loginService.addOrUpdateUsers(loginAddRequest,id, Accesss.USER,gender));

    }

    @Test
    void testGetUser() throws InvocationTargetException, IllegalAccessException {
        //given
        var login = LoginServiceTestDataGenerator.login();
        var loginResponse=LoginServiceTestDataGenerator.loginResponse();
        when(loginRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(login));

        //when
        loginService.getUser(id);

        //then
        Assertions.assertEquals(loginResponse,loginService.getUser(id));
    }



    @Test
    void testDeleteUser(){
        //given
        var login = LoginServiceTestDataGenerator.login();
        when(loginRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(login));

        //When
        loginService.deleteUser(id);

        //then
        verify(loginRepository,times(1)).findByIdAndSoftDeleteIsFalse(id);
    }

    @Test
    void testGetAllUser() throws InvocationTargetException, IllegalAccessException {
        //given
        var login = LoginServiceTestDataGenerator.loginList();
        var loginResponse=LoginServiceTestDataGenerator.loginResponseList();
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
        var login = LoginServiceTestDataGenerator.loginList();
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
        var login = LoginServiceTestDataGenerator.login();
        var loginResponse=LoginServiceTestDataGenerator.loginResponse();
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

        var login = LoginServiceTestDataGenerator.login();
        var loginTokeResponse = LoginServiceTestDataGenerator.loginTokenResponse();
        var loginRequest = LoginRequest.builder().password(Utils.decodeBase64("Sp@31122000")).email(email).build();
        var adminConfiguration = LoginServiceTestDataGenerator.adminConfiguration();
        LoginServiceImpl.passWord();
        /*var emailModel = EmailModel.builder().to(to).cc(Collections.singleton(cc)).message(message).subject(subject).build();*/
        when(loginRepository.findByEmailAndSoftDeleteIsFalse(email)).thenReturn(Optional.ofNullable(login));
        when(adminService.getConfigurationDetails()).thenReturn(adminConfiguration);

        //when
        loginService.userLogin(loginRequest);

        //then
        /*verify(loginRepository,times(1)).findByEmailAndSoftDeleteIsFalse(email);*/
        Assertions.assertEquals(loginTokeResponse,loginService.userLogin(loginRequest));

    }

    @Test
    void testGetValidityOfToken() throws InvocationTargetException, IllegalAccessException {

        //given
        List<String> roles = new ArrayList<>();
        roles.add(Role.ANONYMOUS.toString());
        var jwtUser = JWTUser.builder().id(id).accesss(roles).build();
        String token = jwtTokenUtil.generateToken(jwtUser);
        var login = LoginServiceTestDataGenerator.login();
        var loginResponse = LoginServiceTestDataGenerator.loginResponse();
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
        var login = LoginServiceTestDataGenerator.login();
        var shoppingLogList = LoginServiceTestDataGenerator.shoppingListLog();
        var listLog = LoginServiceTestDataGenerator.shoppingListLogs();
        when(loginRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(login));
        when(listLogRepository.findByLoginIdAndSoftDeleteIsFalse(loginId)).thenReturn(shoppingLogList);

        //when
        loginService.logOut(id);

        //then
        /*verify(loginRepository,times(1)).findByIdAndSoftDeleteIsFalse(id);*/
        /*Assertions.assertEquals(shoppingLogList,recipeService.getShoppingList(id));*/
        verify(listLogRepository).save(listLog);


    }


}
