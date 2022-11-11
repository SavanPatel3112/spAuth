package com.example.authmoduls.service;

import com.example.authmoduls.auth.decorator.*;
import com.example.authmoduls.auth.enums.UserSortBy;
import com.example.authmoduls.auth.enums.UserStatus;
import com.example.authmoduls.auth.model.UserModel;
import com.example.authmoduls.auth.rabbitmq.UserPublisher;
import com.example.authmoduls.auth.repository.userRepository.UserRepository;
import com.example.authmoduls.auth.service.userService.UserService;
import com.example.authmoduls.auth.service.userService.UserServiceImpl;
import com.example.authmoduls.common.decorator.*;
import com.example.authmoduls.common.decorator.RequestSession;
import com.example.authmoduls.common.enums.Role;
import com.example.authmoduls.common.model.*;
import com.example.authmoduls.common.repository.ImportedDataRepository;
import com.example.authmoduls.common.repository.UserDataRepository;
import com.example.authmoduls.common.service.AdminConfigurationService;
import com.example.authmoduls.common.service.SchedulerService;
import com.example.authmoduls.common.utils.JwtTokenUtil;
import com.example.authmoduls.common.utils.PasswordUtils;
import com.example.authmoduls.common.utils.Utils;
import com.example.authmoduls.helper.DataSetHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static org.mockito.Mockito.*;


@AutoConfigureMockMvc
@Slf4j
class UserServiceImplTest {

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
    private final SchedulerService schedulerService = mock(SchedulerService.class);
    private final UserService userService = new UserServiceImpl(userRepository,importedDataRepository,userDataRepository,nullAwareBeanUtilsBean,jwtTokenUtil,passwordUtils,adminService,utils,notificationParser,userPublisher,modelMapper,requestSession,schedulerService){};
    @Test
    void testGetUser() throws InvocationTargetException, IllegalAccessException {
        String id = "121";

        var userResponse = UserResponse.builder().userName("sp3112").build();

        var userModel = UserModel.builder().userName("sp3112").build();

        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        userService.getUser(id);

        Assertions.assertEquals(userResponse,userService.getUser(id));
    }
    @Test
    void testAddOrUpdateUser() throws InvocationTargetException, IllegalAccessException {
        //given
        String id ="123";
        Set<String> requiredEmailItems = Collections.singleton("@");
        String userName = "sp3112";
        String firstName = "Savan";
        String middleName = "Kiritbhai";
        String lastName = "Patel";
        String email = "savan9048@gmail.com";
        String mobileNo = "9081738141";
        String passWord = "Sp@31123112";
        String nameRegex = "^[0-9#$@!%&*?.-_=]{1,15}$";
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        String mobileRegex = "^[0-9]{10}$";
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,15}$";


        var userAddRequest =UserAddRequest.builder().userName(userName).firstName(firstName).middleName(middleName).lastName(lastName).email(email)
                .userName(userName).password(passWord).mobileNo(mobileNo).build();

        var userResponse = UserResponse.builder().userName(userName).firstName(firstName).middleName(middleName).lastName(lastName).email(email)
                .userName(userName).password(passWord).mobileNo(mobileNo).build();

        var userModel =UserModel.builder().userName(userName).firstName(firstName).middleName(middleName).lastName(lastName).email(email)
                .userName(userName).passWord(passWord).mobileNo(mobileNo).build();

        var adminConfiguration = AdminConfiguration.builder().username("sp3112").id(id).nameRegex(nameRegex).emailRegex(emailRegex)
                .moblieNoRegex(mobileRegex).requiredEmailItems(requiredEmailItems).passwordRegex(passwordRegex).build();
        //when
        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        when(adminService.getConfigurationDetails()).thenReturn(adminConfiguration);

        userService.addOrUpdateUser(userAddRequest, null, Role.ADMIN);

        //then
        Assertions.assertEquals(userResponse,userService.addOrUpdateUser(userAddRequest, null, Role.ADMIN));
    }

    @Test
    void testGetAllUser() throws InvocationTargetException, IllegalAccessException {
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
    void testDeleteUser(){
        String id = "111";

        var userModel=UserModel.builder().role(Role.ADMIN).build();

        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        userService.deleteUser(id);

        verify(userRepository,times(1)).findByIdAndSoftDeleteIsFalse(id);
    }
    @Test
    void testGetAllUserByPagination() {
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
    void testGetToken() throws InvocationTargetException, IllegalAccessException {
        String id = "111";
        String userName = "sp3112";
        String firstName = "savan";
        String lastName = "patel";
        String middleName = "kiritbhai";

        var userModel = UserModel.builder().userName(userName).firstName(firstName).lastName(lastName).middleName(middleName).role(Role.ADMIN).build();
        var userResponse = UserResponse.builder().userName(userName).firstName(firstName).lastName(lastName).middleName(middleName).role(Role.ADMIN).build();

        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        userService.getToken(id);

        Assertions.assertEquals(userResponse, userService.getToken(id));
    }

    @Test
    void testGetEncryptPassword() throws InvocationTargetException, IllegalAccessException {
        String id = "111";
        String encryptPassWord = "$2a$12$mlrUaiB1FDvKVFlpm.6l3uEyNHiqBNeQpbmdI3NlHqhCvHuJKZq5O";

        var userModel = UserModel.builder().passWord(PasswordUtils.encryptPassword(encryptPassWord)).build();
        var userResponse = PasswordUtils.encryptPassword(encryptPassWord);
        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        userService.getEncryptPassword(id);

        Assertions.assertEquals(userResponse,userService.getEncryptPassword(id));

    }

    @Test
    void testCheckUserAuthentication() throws NoSuchAlgorithmException, InvocationTargetException, IllegalAccessException {
        String email = "savan89@gmail.com";
        String passWord = "$2a$12$KeRiPdjD1A.AhFO2vzNdPe3/se1XNovi9fv6GIWHC69HgFb3adnM6";

        var userModel = UserModel.builder().email(email).build();
        var userResponse = UserResponse.builder().password(PasswordUtils.encryptPassword(passWord)).build();

        when(userRepository.findByEmailAndSoftDeleteIsFalse(email)).thenReturn(Optional.ofNullable(userModel));
        userService.checkUserAuthentication(email,passWord);

        Assertions.assertEquals(userResponse,userService.checkUserAuthentication(email,passWord));


    }

    @Test
    void testGetIdFromToken() throws InvocationTargetException, IllegalAccessException {

        String id = "62ea7609e4eb6b6d0bf0aebc";
        JWTUser jwtUser = new JWTUser(id);
        String token = jwtTokenUtil.generateToken(jwtUser);

        when(userRepository.existsByIdAndSoftDeleteFalse(id)).thenReturn(true);

        userService.getIdFromToken(token);

        Assertions.assertEquals(token,userService.getIdFromToken(token));


    }

    @Test
    void testGetValidityOfToken() throws InvocationTargetException, IllegalAccessException {
        String id ="62ea7609e4eb6b6d0bf0aebc";
        JWTUser jwtUser = new JWTUser(id);
        String token = jwtTokenUtil.generateToken(jwtUser);

        var userModel = UserModel.builder().token(token).build();

        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));

        userService.getValidityOfToken(token);

        Assertions.assertEquals(token,userService.getValidityOfToken(token));


    }
    @Test
    void testUserLogin() throws InvocationTargetException, IllegalAccessException, NoSuchAlgorithmException {

        String email = "savan9045@gmail.com";
        String passWord = "Sp@31123112";
        String otp = "123456";
        Set<String> requiredEmailItems = Collections.singleton("@");
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,15}$";

        var userModel =  UserModel.builder().email(email).passWord(passWord).otp(otp).build();

        var adminConfiguration = AdminConfiguration.builder().emailRegex(emailRegex)
                .requiredEmailItems(requiredEmailItems).passwordRegex(passwordRegex).build();
        /*passwordUtils.isPasswordAuthenticated(passWord,passWord, PasswordEncryptionType.BCRYPT);*/
        when(userRepository.findByEmailAndSoftDeleteIsFalse(email)).thenReturn(Optional.ofNullable(userModel));
        when(adminService.getConfigurationDetails()).thenReturn(adminConfiguration);

        userService.userLogin(email, passWord);

        verify(userRepository,times(1)).findByEmailAndSoftDeleteIsFalse(email);

    }
    @Test
    void testGetOtp() throws InvocationTargetException, IllegalAccessException {

        String otp = "123456";
        String id = "62ea7609e4eb6b6d0bf0aebc";

        var userModel = UserModel.builder().otp(otp).id(id).build();
        var userResponse =  UserResponse.builder().otp(otp).id(id).build();

        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        when(userRepository.existsByIdAndOtpAndSoftDeleteFalse(id,otp)).thenReturn(true);

        userService.getOtp(otp, id);

        Assertions.assertEquals(userResponse,userService.getOtp(otp, id));

    }
    @Test
    void testForgotPassword(){

        String email ="savan9045@gmail.com";
        String otp = "123456";

        var userModel =  UserModel.builder().email(email).otp(otp).build();

        when(userRepository.findByEmailAndSoftDeleteIsFalse(email)).thenReturn(Optional.ofNullable(userModel));

        userService.forgotPassword(email);

            verify(userRepository,times(1)).findByEmailAndSoftDeleteIsFalse(email);

    }
    @Test
    void testChangePassword() throws NoSuchAlgorithmException {

        String password = "Sp@31123112";
        String confirmPassword = "Pk@123456";
        String newPassword = "Pk@123456";
        String id = "123";

        var userModel = UserModel.builder().passWord(PasswordUtils.encryptPassword(password)).confirmPassWord(confirmPassword)
                .newPassWord(newPassword).id(id).build();

        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));

        userService.changePassword(password, confirmPassword, newPassword, id);

        verify(userRepository,times(1)).findByIdAndSoftDeleteIsFalse(id);
    }
    @Test
    void testLogOut() throws SchedulerException, InvocationTargetException, IllegalAccessException {
       String id = "123";
       boolean login = true;
       Date logoutTime = new Date();

       var userModel = UserModel.builder().id(id).login(login).logoutTime(logoutTime).build();

       when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));

       userService.logOut(id);

       verify(userRepository,times(1)).findByIdAndSoftDeleteIsFalse(id);

    }
    @Test
    void testGetUserByRole(){
        String id =  "123";
        String userName = "sp3112";
        String firstName = "Savan";
        String middleName = "Kiritbhai";
        String lastName = "Patel";
        String email = "savan9045@gmail.com";
        String mobileNo = "9081738141";
        int semester = 1;
        double spi = 7.01;
        int year = 2021;

        var userFilter = UserFilter.builder().role(Role.ADMIN).build();
        var results = List.of(Result.builder().semester(semester).date(new Date()).spi(spi).year(year).build());
        var userResponse =List.of(UserResponse.builder().role(Role.ADMIN).firstName(firstName).middleName(middleName).lastName(lastName).id(id).userName(userName)
                .email(email).mobileNo(mobileNo).results(results).build());

        when(userRepository.getUser(userFilter)).thenReturn(userResponse);

        userService.getUserByRole(userFilter);

        Assertions.assertEquals(userResponse,userService.getUserByRole(userFilter));
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
    void testAddResult() throws InvocationTargetException, IllegalAccessException {

        String id = "123";
        int semester = 1;
        double spi = 9.05;
        int year = 2022;
        Date date = new Date();
        String semesterRegex = "^[0-8]{1}$";
        String spiRegex = "^[0-10]{2}$";
        String status = "active";

        var result = Result.builder().semester(semester).spi(spi).year(year).date(date).status(status).build();
        var userModel = UserModel.builder().role(Role.STUDENT).result(result).build();
        var userResponse = UserResponse.builder().role(Role.STUDENT).result(result).build();
        var adminConfiguration = AdminConfiguration.builder().semesterRegex(semesterRegex).spiRegex(spiRegex).build();

        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        when(adminService.getConfigurationDetails()).thenReturn(adminConfiguration);

        userService.addResult(result,id);

        Assertions.assertEquals(userResponse,userService.addResult(result,id));
    }

    @Test
    void testUserUpdate() throws InvocationTargetException, IllegalAccessException {
        //void userUpdate(String id, Role role, UserAddRequest userAddRequest) throws InvocationTargetException, IllegalAccessException;
        String id =  "123";
        String userName = "sp3112";
        String firstName = "Savan";
        String middleName = "Kiritbhai";
        String lastName = "Patel";
        String email = "savan9045@gmail.com";
        String mobileNo = "9081738141";
        Role role = Role.ADMIN;
        String from = "savan.p@techroversolutions.com";
        String message = "deleted successfully";
        String to = "savan.p@techroversolutions.com";
        String cc = "savan.p@techroversolutions.com";
        String subject = "user";
        var userModel = UserModel.builder().firstName(firstName).middleName(middleName).lastName(lastName).userName(userName)
                                                    .email(email).mobileNo(mobileNo).build();
        var emailModel =EmailModel.builder().cc(Collections.singleton(cc)).subject(subject).to(to).message(message).build();
        var userAddRequest =UserAddRequest.builder().firstName(firstName).middleName(middleName).lastName(lastName).userName(userName)
                                                    .email(email).mobileNo(mobileNo).build();
        var adminConfiguration = AdminConfiguration.builder().from(from).build();

        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        when(adminService.getConfigurationDetails()).thenReturn(adminConfiguration);

        utils.sendEmailNow(emailModel);
        userService.userUpdate(id, role, userAddRequest);

        verify(userRepository,times(1)).findByIdAndSoftDeleteIsFalse(id);
    }

    @Test
    void testUserDelete() throws InvocationTargetException, IllegalAccessException {
        String id =  "123";
        Role role = Role.ADMIN;
        String from = "savan.p@techroversolutions.com";
        String message = "deleted successfully";
        String to = "savan.p@techroversolutions.com";
        String cc = "savan.p@techroversolutions.com";
        String subject = "user";
        var userModel =  UserModel.builder().id(id).role(role).build();
        var adminConfiguration = AdminConfiguration.builder().from(from).build();
        var emailModel =EmailModel.builder().cc(Collections.singleton(cc)).subject(subject).to(to).message(message).build();
        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        when(adminService.getConfigurationDetails()).thenReturn(adminConfiguration);

        utils.sendEmailNow(emailModel);

        userService.userDelete(id, role);

        verify(userRepository,times(1)).findByIdAndSoftDeleteIsFalse(id);
    }
    @Test
    void testUploadFile(){
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
    void testImportUsers(){
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
    void testImportUsersVerify(){
        String id = "13";
        var userImportVerifyRequest = UserImportVerifyRequest.builder().id(id).build();
        var userDataModel = List.of(UserDataModel.builder().id(id).build());
        var userImportedData = UserImportedData.builder().id(id).build();
        when(importedDataRepository.findById(id)).thenReturn(Optional.ofNullable(userImportedData));

        userService.importUsersVerify(userImportVerifyRequest);

        Assertions.assertEquals(userDataModel,userService.importUsersVerify(userImportVerifyRequest));
    }
    @Test
    void testGetUserPassword() throws InvocationTargetException, IllegalAccessException {
        String otp = "123456";
        String password = "Sp@31122000";
        String confirmPassword = "Sp@31122000";
        String userName = "Sp@3112";
        String from = "savan.p@techroversolutions.com";
        String message = "deleted successfully";
        String to = "savan.p@techroversolutions.com";
        String cc = "savan.p@techroversolutions.com";
        String subject = "user";
        var userModel =UserModel.builder().userName(userName).otp(otp).passWord(PasswordUtils.encryptPassword(password)).confirmPassWord(PasswordUtils.encryptPassword(confirmPassword)).build();
        var adminConfiguration = AdminConfiguration.builder().from(from).build();
        var emailModel = EmailModel.builder().to(to).cc(Collections.singleton(cc)).subject(subject).message(message).build();

        when(userRepository.findByUserNameAndSoftDeleteIsFalse(userName)).thenReturn(Optional.ofNullable(userModel));
        when(adminService.getConfigurationDetails()).thenReturn(adminConfiguration);

        utils.sendOtp(userModel, confirmPassword);
        utils.sendEmailNow(emailModel);

        userService.getUserPassword(userName, password, confirmPassword);

        verify(userRepository,times(1)).findByUserNameAndSoftDeleteIsFalse(userName);

    }
    @Test
    void testSendMailToInvitedUser() throws InvocationTargetException, IllegalAccessException {
        String from = "savan.p@techroversolutions.com";
        String message = "deleted successfully";
        String to = "savan.p@techroversolutions.com";
        String cc = "savan.p@techroversolutions.com";
        String subject = "user";
        String userName = "Sp@3112";

        var userModel = List.of(UserModel.builder().userName(userName).userStatus(UserStatus.INVITED).build());
        var userModel1 = UserModel.builder().userName(userName).build();
        var adminConfiguration = AdminConfiguration.builder().from(from).build();
        var emailModel = EmailModel.builder().subject(subject).cc(Collections.singleton(cc)).to(to).message(message).build();

        when(userRepository.findByUserStatusAndSoftDeleteIsFalse(UserStatus.INVITED)).thenReturn(userModel);
        when(adminService.getConfigurationDetails()).thenReturn(adminConfiguration);
        when(modelMapper.map(userModel, UserModel.class)).thenReturn(userModel1);

        utils.sendEmailNow(emailModel);

        userService.sendMailToInvitedUser(UserStatus.INVITED);

        verify(userRepository,times(1)).findByUserStatusAndSoftDeleteIsFalse(UserStatus.INVITED);

    }
    @Test
    void testUserChartApi() throws InvocationTargetException, IllegalAccessException {

        String userId = "62ea7609e4eb6b6d0bf0aebc";
        int year = 2021;
        String title = "All Data";
        int totalCount = 10;
        var userDateDetails = List.of(UserDateDetails.builder().userIds(Collections.singleton(userId)).year(year).build());
        var monthAndYear = MonthAndYear.builder().title(Collections.singleton(title)).totalCount(totalCount).userDateDetails(userDateDetails).build();

        when(userRepository.userChartApi(year)).thenReturn(userDateDetails);

        userService.userChartApi(year);

        Assertions.assertEquals(userDateDetails,userService.userChartApi(year));


    }
}

