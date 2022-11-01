package com.example.authmoduls.service;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.example.authmoduls.auth.decorator.*;
import com.example.authmoduls.auth.enums.UserSortBy;
import com.example.authmoduls.auth.model.UserModel;
import com.example.authmoduls.auth.rabbitmq.UserPublisher;
import com.example.authmoduls.auth.repository.userRepository.UserRepository;
import com.example.authmoduls.auth.service.userService.UserService;
import com.example.authmoduls.auth.service.userService.UserServiceImpl;
import com.example.authmoduls.common.decorator.*;
import com.example.authmoduls.common.enums.Role;
import com.example.authmoduls.common.model.AdminConfiguration;
import com.example.authmoduls.common.model.JWTUser;
import com.example.authmoduls.common.repository.ImportedDataRepository;
import com.example.authmoduls.common.repository.UserDataRepository;
import com.example.authmoduls.common.service.AdminConfigurationService;
import com.example.authmoduls.common.utils.JwtTokenUtil;
import com.example.authmoduls.common.utils.PasswordUtils;
import com.example.authmoduls.common.utils.Utils;
import com.example.authmoduls.helper.DataSetHelper;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.model.DateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.mockito.Mockito.*;


@AutoConfigureMockMvc
@Slf4j
public class UserServiceImplTest {

    @Autowired
    DataSetHelper dataSetHelper;

    private final UserRepository userRepository = mock(UserRepository.class);
    private final ImportedDataRepository importedDataRepository = mock(ImportedDataRepository.class);
    private final UserDataRepository userDataRepository = mock(UserDataRepository.class);
    private final NullAwareBeanUtilsBean nullAwareBeanUtilsBean = mock(NullAwareBeanUtilsBean.class);
    private final JwtTokenUtil jwtTokenUtil = mock(JwtTokenUtil.class);
    private final PasswordUtils passwordUtils = mock(PasswordUtils.class);
    private final AdminConfigurationService adminService = mock(AdminConfigurationService.class);
    private final Utils utils = mock(Utils.class);
    private final NotificationParser notificationParser = mock(NotificationParser.class);
    private final UserPublisher userPublisher = mock(UserPublisher.class);
    private final ModelMapper modelMapper = mock(ModelMapper.class);
    private final RequestSession requestSession = mock(RequestSession.class);
    private final UserService userService = new UserServiceImpl(userRepository,importedDataRepository,userDataRepository,nullAwareBeanUtilsBean,jwtTokenUtil,passwordUtils,adminService,utils,notificationParser,userPublisher,modelMapper,requestSession){};
    @Test
    public void getUser(){
        try {

            UserModel userModel = dataSetHelper.getUserModel();
            when(userRepository.findByIdAndSoftDeleteIsFalse(userModel.getId()).get()).thenReturn(userModel);
            Assertions.assertEquals(userService.getUser(userModel.getId()), new UserResponse());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    void testGetUserResult() throws InvocationTargetException, IllegalAccessException {
        //given
        String userId =  "123";
        int semester = 1;

        var userDetail = UserDetail.builder().userIds(Collections.singleton(userId)).semester(semester).build();

        var results = List.of(Result.builder().semester(1).date(new Date()).spi(7.1).year(2022).build());

        var userDetailResponse = List.of(UserDetailResponse.builder().results(results).cgpa(7.1).build());

        when(userRepository.getUserResult(userDetail)).thenReturn(userDetailResponse);

        //when
        userService.getUserResult(userDetail);

        //then
        Assertions.assertEquals(userDetailResponse,userService.getUserResult(userDetail));
    }

    @Test
    void testAddOrUpdateUser() throws InvocationTargetException, IllegalAccessException {

        //given
        String id ="123";
        Set<String> requiredEmailItems = Collections.singleton("@");
        var userAddRequest =UserAddRequest.builder().userName("sp3112").firstName("Savan").middleName("kiritbhai").lastName("patel").email("savan9045@gmail.com")
                .userName("sp3112").password("Sp@31123112").mobileNo("9081738141").build();

        var results = List.of(Result.builder().semester(1).date(new Date()).spi(7.1).year(2022).build());

        var userResponse = UserResponse.builder().firstName("Savan").middleName("kiritbhai").lastName("patel").results(results)
                .userName("sp3112").email("savan9045@gmail.com").password("Sp@31123112").role(Role.ADMIN)
                .mobileNo("9081738141").balance(665.2).build();

        var userModel =UserModel.builder().firstName("Savan").middleName("Kiritbhai").lastName("Patel").build();

        var adminConfiguration = AdminConfiguration.builder().username("sp3112").id(id).nameRegex("^[0-9#$@!%&*?.-_=]{1,15}$").emailRegex("^[A-Za-z0-9+_.-]+@(.+)$")
                .moblieNoRegex("^[0-9]{10}$").regex("^(?=.{1,64}@)[a-z0-9_-]+(\\\\.[a-z0-9_-]+)*@\"\n" +
                        "                + \"[^-][a-z0-9-]+(\\\\.[a-z0-9-]+)*(\\\\.[a-z]{2,})$").spiRegex("^[0-10]{2}$").semesterRegex("^[0-8]{1}$")
                .requiredEmailItems(requiredEmailItems).passwordRegex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,15}$").build();
        //when
        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        when(adminService.getConfigurationDetails()).thenReturn(adminConfiguration);

        userService.addOrUpdateUser(userAddRequest, null, Role.ADMIN);

        //then
        Assertions.assertEquals(userResponse,userService.addOrUpdateUser(userAddRequest, null, Role.ADMIN));
    }

    @Test
    void getAllUser() throws InvocationTargetException, IllegalAccessException {
        //given
        var userResponse = List.of(UserResponse.builder().role(Role.ADMIN).build());

        var userModel=List.of(UserModel.builder().role(Role.ADMIN).build());
        //when
        when(userRepository.findAllBySoftDeleteFalse()).thenReturn(userModel);
        userService.getAllUser();
        //then
        Assertions.assertEquals(userResponse,userService.getAllUser());
    }

    @Test
    public void deleteUser(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            when(userRepository.findByIdAndSoftDeleteIsFalse(userModel.getId())).thenReturn(Optional.of(userModel));
            userService.deleteUser(userModel.getId());
            verify(userRepository, times(1)).save(userModel);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void getAllUserByPagination() throws InvocationTargetException, IllegalAccessException {
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            UserFilter userFilter = new UserFilter();
            userFilter.setRole(userFilter.getRole());
            FilterSortRequest.SortRequest<UserSortBy> sort = new FilterSortRequest.SortRequest<>();
            sort.setSortBy(UserSortBy.EMAIL);
            sort.setOrderBy(Sort.Direction.ASC);
            Pagination pagination = new Pagination();
            pagination.setLimit(1);
            pagination.setPage(1);
            PageRequest pageRequest = PageRequest.of(pagination.getLimit(), pagination.getPage());
            List<UserModel> userModels1 = new ArrayList<>();
            userModels1.add(userModel);
            when(userRepository.findAllUserByFilterAndSortAndPage(userFilter, sort, pageRequest).getContent()).thenReturn(userModels1);
            Assertions.assertEquals(userService.getAllUserByFilterAndSortAndPage(userFilter, sort, pageRequest), new PageResponse<UserModel>());
            verify(userRepository, times(1)).findAllUserByFilterAndSortAndPage(userFilter, sort, pageRequest);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void getToken() throws InvocationTargetException, IllegalAccessException {
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            List<UserModel> userModels = new ArrayList<>();
            userModels.add(userModel);
            when(userRepository.findAllBySoftDeleteFalse()).thenReturn(userModels);
            Assertions.assertEquals(userService.getToken(userModel.getId()), new UserResponse());
            verify(userRepository,times(1)).save(userModel);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void getEncryptPassword(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            List<UserModel> userModels = new ArrayList<>();
            userModels.add(userModel);
            when(userRepository.findAllBySoftDeleteFalse()).thenReturn(userModels);
            Assertions.assertEquals(userService.getEncryptPassword(userModel.getId()), PasswordUtils.encryptPassword(""));
            verify(userRepository,times(1)).save(userModel);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void checkUserAuthentication(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            when(userRepository.findByEmailAndSoftDeleteIsFalse(userModel.getEmail()).get()).thenReturn(userModel);
            Assertions.assertEquals(userService.checkUserAuthentication(userModel.getEmail(),userModel.getPassWord()),new UserResponse());
            verify(userRepository,times(1)).findByEmailAndSoftDeleteIsFalse(userModel.getEmail());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void getIdFromToken(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            JWTUser jwtUser = new JWTUser(userModel.getId(), Collections.singletonList(userModel.getRole().toString()));
            String token = jwtTokenUtil.generateToken(jwtUser);
            when(userRepository.existsByIdAndSoftDeleteFalse(userModel.getId())).thenReturn(true);
            Assertions.assertEquals(userService.getIdFromToken(token),userModel.getId());
            verify(userRepository,times(1)).existsByIdAndSoftDeleteFalse(userModel.getId());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void getValidityOfToken(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            JWTUser jwtUser = new JWTUser(userModel.getId(), Collections.singletonList(userModel.getRole().toString()));
            String token = jwtTokenUtil.generateToken(jwtUser);
            when(userRepository.existsByIdAndSoftDeleteFalse(userModel.getId())).thenReturn(true);
            Assertions.assertEquals(userService.getValidityOfToken(token),userModel.getId());
            verify(userRepository,times(1)).existsByIdAndSoftDeleteFalse(userModel.getId());
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }
    @Test
    public void userLogin(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            when(userRepository.findByEmailAndSoftDeleteIsFalse(userModel.getEmail())).thenReturn(Optional.of(userModel));
            verify(userRepository,times(1)).findByEmailAndSoftDeleteIsFalse(userModel.getEmail());
        } catch (Exception e){
                Assertions.fail(e.getMessage());
        }
    }
    @Test
    public void getOtp(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            when(userRepository.existsByIdAndOtpAndSoftDeleteFalse(userModel.getId(),userModel.getOtp())).thenReturn( true);
            verify(userRepository,times(1)).findByEmailAndSoftDeleteIsFalse(userModel.getEmail());
        } catch (Exception e){
                Assertions.fail(e.getMessage());
        }
    }
    @Test
    public void forgotPassword(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            when(userRepository.findByEmailAndSoftDeleteIsFalse(userModel.getEmail()).get()).thenReturn( userModel);

            verify(userRepository,times(1)).findByEmailAndSoftDeleteIsFalse(userModel.getEmail());
        } catch (Exception e){
                Assertions.fail(e.getMessage());
        }
    }
    @Test
    public void setPassword(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            when(userRepository.findByIdAndSoftDeleteIsFalse(userModel.getId())).thenReturn(Optional.of(userModel));
            verify(userRepository).findByIdAndSoftDeleteIsFalse(userModel.getId());
        } catch (Exception e){
                Assertions.fail(e.getMessage());
        }
    }
    @Test
    public void logOut(){
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            when(userRepository.findByIdAndSoftDeleteIsFalse(userModel.getId())).thenReturn(Optional.of(userModel));
            verify(userRepository).findByIdAndSoftDeleteIsFalse(userModel.getId());
        } catch (Exception e){
                Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void getUserByRole(){
        try {
            UserFilter userFilter = new UserFilter();
            userFilter.setRole(userFilter.getRole());
            List<UserResponse> userResponses = new ArrayList<>();
            when(userRepository.getUser(userFilter)).thenReturn(userResponses);
            Assertions.assertEquals(userService.getUserByRole(userFilter),userResponses);
            verify(userRepository,times(1)).getUser(userFilter);
        } catch (Exception e){
                Assertions.fail(e.getMessage());
        }
    }


}

