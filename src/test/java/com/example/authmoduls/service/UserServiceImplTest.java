package com.example.authmoduls.service;

import com.example.authmoduls.auth.decorator.*;
import com.example.authmoduls.auth.enums.UserSortBy;
import com.example.authmoduls.auth.enums.UserStatus;
import com.example.authmoduls.auth.model.UserModel;
import com.example.authmoduls.auth.rabbitmq.UserPublisher;
import com.example.authmoduls.auth.repository.userRepository.UserRepository;
import com.example.authmoduls.auth.service.userService.UserService;
import com.example.authmoduls.auth.service.userService.UserServiceImpl;
import com.example.authmoduls.common.decorator.RequestSession;
import com.example.authmoduls.common.decorator.*;
import com.example.authmoduls.common.enums.PasswordEncryptionType;
import com.example.authmoduls.common.enums.Role;
import com.example.authmoduls.common.model.*;
import com.example.authmoduls.common.repository.ImportedDataRepository;
import com.example.authmoduls.common.repository.UserDataRepository;
import com.example.authmoduls.common.service.AdminConfigurationService;
import com.example.authmoduls.common.service.SchedulerService;
import com.example.authmoduls.common.utils.JwtTokenUtil;
import com.example.authmoduls.common.utils.PasswordUtils;
import com.example.authmoduls.common.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.quartz.SchedulerException;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import static org.springframework.data.mongodb.core.aggregation.DateOperators.Month.month;

@AutoConfigureMockMvc
@Slf4j
class UserServiceImplTest {
    private static final String id = "62ea7609e4eb6b6d0bf0aebc";
    private static final String userName = "sp3112";
    private static final String firstName = "Savan";
    private static final String middleName = "Kiritbhai";
    private static final String lastName = "Patel";
    private static final String fullName = "Savan Kiritbhai Patel";
    private static final String email = "savan9045@gmail.com";
    private static final String emails = "savan89@gmail.com";
    private static final String mobileNo = "9081738141";
    private static final Role role = Role.ADMIN;
    private static final String password = "Techrover@2023";
    private static final String passWord = "Sp@31122000";
    private static final String newPassword = "Sp@31122000";
    private static final String confirmPassword = "Sp@31122000";
    private static final String otp = "123456";
    private static final String from = "savan.p@techroversolutions.com";
    private static final String message = "deleted successfully";
    private static final String to = "savan.p@techroversolutions.com";
    private static final String cc = "savan.p@techroversolutions.com";
    private static final String subject = "user";
    private static final String nameRegex = "^[0-9#$@!%&*?.-_=]{1,15}$";
    private static final String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String mobileRegex = "^[0-9]{10}$";
    private static final String requiredEmailItems = "@";
    private static final int semester = 1;
    private static final double spi = 9.05;
    private static final double cgpi = 9.05;
    private static final int year = 2022;
    private static final Date date = new Date();
    private static final String semesterRegex = "^[0-8]{1}$";
    private static final String spiRegex = "^[0-10]{2}$";
    private static final String status = String.valueOf(UserStatus.ACTIVE);
    private static final String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,15}$";
    private static final String host = "smtp.office365.com";
    private static final String port = "587";
    private static final String title = "All Data";
    private final UserRepository userRepository = mock(UserRepository.class);
    private final ImportedDataRepository importedDataRepository = mock(ImportedDataRepository.class);
    private final UserDataRepository userDataRepository = mock(UserDataRepository.class);
    private final NullAwareBeanUtilsBean nullAwareBeanUtilsBean = mock(NullAwareBeanUtilsBean.class);
    private final JwtTokenUtil jwtTokenUtil = mock(JwtTokenUtil.class);
    private final PasswordUtils passwordUtils = mock(PasswordUtils.class);
    private final AdminConfigurationService adminService = mock(AdminConfigurationService.class);
    private final Utils utils = spy(Utils.class);
    private final NotificationParser notificationParser = mock(NotificationParser.class);
    private final UserPublisher userPublisher = mock(UserPublisher.class);
    private final ModelMapper modelMapper = mock(ModelMapper.class);
    private final RequestSession requestSession = mock(RequestSession.class);
    private final SchedulerService schedulerService = mock(SchedulerService.class);
    private final UserService userService = new UserServiceImpl(userRepository, importedDataRepository, userDataRepository, nullAwareBeanUtilsBean, jwtTokenUtil, passwordUtils, adminService, utils, notificationParser, userPublisher, modelMapper, requestSession);

    @Test
    void testGetUser() throws InvocationTargetException, IllegalAccessException {
        var userResponse = UserResponse.builder().userName(userName).build();
        var userModel = UserModel.builder().userName(userName).build();
        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        userService.getUser(id);
        Assertions.assertEquals(userResponse, userService.getUser(id));
    }

    @Test
    void testAddOrUpdateUser() throws InvocationTargetException, IllegalAccessException {
        //given
        var userAddRequest = UserAddRequest.builder().userName(userName).firstName(firstName).middleName(middleName).lastName(lastName).email(email).userName(userName).password(password).mobileNo(mobileNo).build();
        var userResponse = UserResponse.builder().userName(userName).firstName(firstName).middleName(middleName).lastName(lastName).email(email).userName(userName).password(password).mobileNo(mobileNo).build();
        var userModel = UserModel.builder().userName(userName).firstName(firstName).middleName(middleName).lastName(lastName).email(email).userName(userName).passWord(password).mobileNo(mobileNo).build();
        var adminConfiguration = AdminConfiguration.builder().username(userName).id(id).nameRegex(nameRegex).emailRegex(emailRegex).mobileNoRegex(mobileRegex).requiredEmailItems(Collections.singleton(requiredEmailItems)).passwordRegex(passwordRegex).build();
        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        when(adminService.getConfigurationDetails()).thenReturn(adminConfiguration);
        //when
        nullAwareBeanUtilsBean.copyProperties(userResponse, userModel);
        userService.addOrUpdateUser(userAddRequest, id, Role.ADMIN);
        when(modelMapper.map(userModel,UserResponse.class)).thenReturn(userResponse);
        //then
        Assertions.assertEquals(userResponse, userService.addOrUpdateUser(userAddRequest, id, Role.ADMIN));
    }

    @Test
    void testGetAllUser() throws InvocationTargetException, IllegalAccessException {
        //given
        var userResponse = List.of(UserResponse.builder().role(Role.ADMIN).build());
        var userModel = List.of(UserModel.builder().role(Role.ADMIN).build());
        when(userRepository.findAllBySoftDeleteFalse()).thenReturn(userModel);
        //when
        userService.getAllUser();
        //then
        Assertions.assertEquals(userResponse, userService.getAllUser());
    }

    @Test
    void testDeleteUser() {
        String id = "111";
        var userModel = UserModel.builder().role(Role.ADMIN).build();
        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        userService.deleteUser(id);
        verify(userRepository, times(1)).findByIdAndSoftDeleteIsFalse(id);
    }

    @Test
    void testGetAllUserByPagination() throws InvocationTargetException, IllegalAccessException {
        UserFilter userFilter = new UserFilter();
        userFilter.setRole(userFilter.getRole());
        FilterSortRequest.SortRequest<UserSortBy> sort = new FilterSortRequest.SortRequest<>();
        sort.setSortBy(UserSortBy.EMAIL);
        sort.setOrderBy(Sort.Direction.ASC);
        Pagination pagination = new Pagination();
        pagination.setLimit(1);
        pagination.setPage(1);
        PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getLimit());
        var userModel = List.of(UserModel.builder().userName(userName).build());
        Page<UserModel> page = new PageImpl<>(userModel);
        when(userRepository.findAllUserByFilterAndSortAndPage(userFilter, sort, pageRequest)).thenReturn(page);
        userService.getAllUserByFilterAndSortAndPage(userFilter, sort, pageRequest);
        Assertions.assertEquals(page, userService.getAllUserByFilterAndSortAndPage(userFilter, sort, pageRequest));
    }

    @Test
    void testGetToken() throws InvocationTargetException, IllegalAccessException {
        var userModel = UserModel.builder().userName(userName).firstName(firstName).lastName(lastName).middleName(middleName).role(Role.ADMIN).build();
        var userResponse = UserResponse.builder().userName(userName).firstName(firstName).lastName(lastName).middleName(middleName).role(Role.ADMIN).build();
        List<String> roles = new ArrayList<>();
        roles.add(Role.ADMIN.toString());
        var jwtUser = JWTUser.builder().id(id).role(roles).build();
        String token = jwtTokenUtil.generateToken(jwtUser);
        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        when(jwtTokenUtil.generateToken(jwtUser)).thenReturn(token);
        userService.getToken(id);
        Assertions.assertEquals(userResponse, userService.getToken(id));
    }

    @Test
    void testGetEncryptPassword() throws InvocationTargetException, IllegalAccessException {
        var userModel = UserModel.builder().id(id).passWord(PasswordUtils.encryptPassword(password)).build();
        var userResponse = UserResponse.builder().id(id).password(PasswordUtils.encryptPassword(password)).build();
        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        nullAwareBeanUtilsBean.copyProperties(userResponse, userModel);
        userService.getEncryptPassword(id);
        Assertions.assertEquals("$2a$12$KeRiPdjD1A.AhFO2vzNdPe3/se1XNovi9fv6GIWHC69HgFb3adnM6", userService.getEncryptPassword(id));
    }

    @Test
    void testCheckUserAuthentication() throws NoSuchAlgorithmException, InvocationTargetException, IllegalAccessException {
        var userModel = UserModel.builder().email(email).passWord(PasswordUtils.encryptPassword(password)).build();
        var userResponse = UserResponse.builder().email(email).password(PasswordUtils.encryptPassword(password)).build();
        when(userRepository.findByEmailAndSoftDeleteIsFalse(email)).thenReturn(Optional.ofNullable(userModel));
        when(passwordUtils.isPasswordAuthenticated(PasswordUtils.encryptPassword(password), PasswordUtils.encryptPassword(password), PasswordEncryptionType.BCRYPT)).thenReturn(true);
        nullAwareBeanUtilsBean.copyProperties(userResponse, userModel);
        userService.checkUserAuthentication(email, password);
        Assertions.assertEquals(userResponse, userService.checkUserAuthentication(email, password));
    }

    @Test
    void testGetIdFromToken() throws InvocationTargetException, IllegalAccessException {
        var jwtUser = JWTUser.builder().id(id).build();
        String token = jwtTokenUtil.generateToken(jwtUser);
        when(userRepository.existsByIdAndSoftDeleteFalse(id)).thenReturn(true);
        userService.getIdFromToken(token);
        Assertions.assertEquals(userService.getIdFromToken(token), token);
    }

    @Test
    void testGetValidityOfToken() throws InvocationTargetException, IllegalAccessException {
        JWTUser jwtUser = new JWTUser(id);
        String token = jwtTokenUtil.generateToken(jwtUser);
        var userModel = UserModel.builder().token(token).build();
        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        userService.getValidityOfToken(token);
        Assertions.assertEquals(token, userService.getValidityOfToken(token));
    }
    @Test
    void testUserLogin() throws InvocationTargetException, IllegalAccessException, NoSuchAlgorithmException {
        var userModel = UserModel.builder().email(emails).passWord(PasswordUtils.encryptPassword(passWord)).login(true).date(date).otp(otp).role(role).state(status).build();
        var adminConfiguration = AdminConfiguration.builder().port(port).host(host).from(from).passwordRegex(passwordRegex)
                                                                            .emailRegex(emailRegex).requiredEmailItems(Collections.singleton(requiredEmailItems)).build();
        var emailModel = EmailModel.builder().cc(Collections.singleton(cc)).message(otp).subject(subject).to(to).build();
        when(userRepository.findByEmailAndSoftDeleteIsFalse(emails)).thenReturn(Optional.ofNullable(userModel));
        when(adminService.getConfigurationDetails()).thenReturn(adminConfiguration);
        when(passwordUtils.isPasswordAuthenticated(PasswordUtils.encryptPassword(passWord), PasswordUtils.encryptPassword(passWord), PasswordEncryptionType.BCRYPT)).thenReturn(true);
        utils.sendEmailNow(emailModel);
        userService.userLogin(emails, PasswordUtils.encryptPassword(passWord));
        verify(userRepository,times(1)).findByEmailAndSoftDeleteIsFalse(emails);
    }

    @Test
    void testGetOtp() throws InvocationTargetException, IllegalAccessException {
        var userModel = UserModel.builder().otp(otp).id(id).build();
        var userResponse = UserResponse.builder().otp(otp).id(id).build();
        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        when(userRepository.existsByIdAndOtpAndSoftDeleteFalse(id, otp)).thenReturn(true);
        userService.getOtp(otp, id);
        Assertions.assertEquals(userResponse, userService.getOtp(otp, id));
    }

    @Test
    void testForgotPassword() {
        var userModel = UserModel.builder().email(email).otp(otp).build();
        when(userRepository.findByEmailAndSoftDeleteIsFalse(email)).thenReturn(Optional.ofNullable(userModel));
        userService.forgotPassword(email);
        verify(userRepository, times(1)).findByEmailAndSoftDeleteIsFalse(email);
    }

    @Test
    void testChangePassword() throws NoSuchAlgorithmException {
        var userModel = UserModel.builder().passWord(PasswordUtils.encryptPassword(password)).confirmPassWord(PasswordUtils.encryptPassword(confirmPassword)).newPassWord(newPassword).id(id).build();
        when(passwordUtils.isPasswordAuthenticated(PasswordUtils.encryptPassword(password), PasswordUtils.encryptPassword(password), PasswordEncryptionType.BCRYPT)).thenReturn(true);
        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        userService.changePassword(PasswordUtils.encryptPassword(password), PasswordUtils.encryptPassword(confirmPassword), newPassword, id);
        verify(userRepository, times(1)).findByIdAndSoftDeleteIsFalse(id);
    }

    @Test
    void testLogOut() throws SchedulerException, InvocationTargetException, IllegalAccessException {
        boolean login = true;
        Date logoutTime = new Date();
        var userModel = UserModel.builder().id(id).login(login).logoutTime(logoutTime).build();
        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        userService.logOut(id);
        verify(userRepository, times(1)).findByIdAndSoftDeleteIsFalse(id);
    }

    @Test
    void testGetUserByRole() {
        var userFilter = UserFilter.builder().role(Role.ADMIN).build();
        var results = List.of(Result.builder().semester(semester).date(new Date()).spi(spi).year(year).build());
        var userResponse = List.of(UserResponse.builder().role(Role.ADMIN).firstName(firstName).middleName(middleName).lastName(lastName).id(id).userName(userName).email(email).mobileNo(mobileNo).results(results).build());
        when(userRepository.getUser(userFilter)).thenReturn(userResponse);
        userService.getUserByRole(userFilter);
        Assertions.assertEquals(userResponse, userService.getUserByRole(userFilter));
    }

    @Test
    void testAddResult() throws InvocationTargetException, IllegalAccessException {
        var result = Result.builder().semester(semester).spi(spi).year(year).date(date).status(status).build();
        var userModel = UserModel.builder().role(Role.STUDENT).result(result).build();
        var userResponse = UserResponse.builder().role(Role.STUDENT).result(result).build();
        var adminConfiguration = AdminConfiguration.builder().semesterRegex(semesterRegex).spiRegex(spiRegex).build();
        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        when(adminService.getConfigurationDetails()).thenReturn(adminConfiguration);
        userService.addResult(result, id);
        Assertions.assertEquals(userResponse, userService.addResult(result, id));
    }

    @Test
    void testGetUserResult() throws InvocationTargetException, IllegalAccessException {
        //given
        var userDetail = UserDetail.builder().userIds(Collections.singleton(id)).semester(semester).build();
        var results = List.of(Result.builder().semester(1).date(new Date()).spi(7.1).year(2022).build());
        var userDetailResponse = List.of(UserDetailResponse.builder().results(results).cgpa(7.1).build());
        when(userRepository.getUserResult(userDetail)).thenReturn(userDetailResponse);
        //when
        userService.getUserResult(userDetail);
        //then
        Assertions.assertEquals(userDetailResponse, userService.getUserResult(userDetail));
    }

    @Test
    void testUserUpdate() throws InvocationTargetException, IllegalAccessException {
        //given
        var userModel = UserModel.builder().firstName(firstName).middleName(middleName).lastName(lastName).userName(userName).email(email).mobileNo(mobileNo).build();
        var emailModel = EmailModel.builder().cc(Collections.singleton(cc)).subject(subject).to(to).message(message).build();
        var userAddRequest = UserAddRequest.builder().firstName(firstName).middleName(middleName).lastName(lastName).userName(userName).email(email).mobileNo(mobileNo).build();
        var adminConfiguration = AdminConfiguration.builder().from(from).build();
        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        when(adminService.getConfigurationDetails()).thenReturn(adminConfiguration);
        utils.sendEmailNow(emailModel);
        //when
        userService.userUpdate(id, role, userAddRequest);
        //then
        verify(userRepository, times(1)).findByIdAndSoftDeleteIsFalse(id);
    }

    @Test
    void testUserDelete() throws InvocationTargetException, IllegalAccessException {
        var userModel = UserModel.builder().id(id).role(role).build();
        var adminConfiguration = AdminConfiguration.builder().from(from).build();
        var emailModel = EmailModel.builder().cc(Collections.singleton(cc)).subject(subject).to(to).message(message).build();
        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        when(adminService.getConfigurationDetails()).thenReturn(adminConfiguration);
        utils.sendEmailNow(emailModel);
        userService.userDelete(id, role);
        verify(userRepository, times(1)).findByIdAndSoftDeleteIsFalse(id);
    }

    @Test
    void testUploadFile() {
        try {
            File file = new File("E:\\java\\randomdata.xlsx");
            FileInputStream inputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), MediaType.MULTIPART_FORM_DATA.toString(), IOUtils.toByteArray(inputStream));
            userService.uploadFile(multipartFile);
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void testImportUsersVerify() {
        var userImportVerifyRequest = UserImportVerifyRequest.builder().id(id).build();
        var userDataModel = List.of(UserDataModel.builder().id(id).build());
        var userImportedData = UserImportedData.builder().id(id).build();
        when(importedDataRepository.findById(id)).thenReturn(Optional.ofNullable(userImportedData));
        userService.importUsersVerify(userImportVerifyRequest);
        Assertions.assertEquals(userDataModel, userService.importUsersVerify(userImportVerifyRequest));
    }

    @Test
    void testGetUserPassword() throws InvocationTargetException, IllegalAccessException {
        var userModel = UserModel.builder().userName(userName).otp(otp).passWord(PasswordUtils.encryptPassword(password)).confirmPassWord(PasswordUtils.encryptPassword(confirmPassword)).build();
        var adminConfiguration = AdminConfiguration.builder().from(from).password(password).host(host).port(port).build();
        var emailModel = EmailModel.builder().to(to).cc(Collections.singleton(cc)).subject(subject).message(message).build();
        when(userRepository.findByUserNameAndSoftDeleteIsFalse(userName)).thenReturn(Optional.ofNullable(userModel));
        when(adminService.getConfigurationDetails()).thenReturn(adminConfiguration);
        assert userModel !=null;
        utils.sendOtp(userModel, confirmPassword);
        utils.sendEmailNow(emailModel);
        userService.getUserPassword(userName, password, confirmPassword);
        verify(userRepository, times(1)).findByUserNameAndSoftDeleteIsFalse(userName);
    }

    @Test
    void testSendMailToInvitedUser() throws InvocationTargetException, IllegalAccessException {
        var userModel = List.of(UserModel.builder().userName(userName).userStatus(UserStatus.INVITED).build());
        var userModel1 = UserModel.builder().userName(userName).build();
        var adminConfiguration = AdminConfiguration.builder().from(from).build();
        var emailModel = EmailModel.builder().subject(subject).cc(Collections.singleton(cc)).to(to).message(message).build();
        when(userRepository.findByUserStatusAndSoftDeleteIsFalse(UserStatus.INVITED)).thenReturn(userModel);
        when(adminService.getConfigurationDetails()).thenReturn(adminConfiguration);
        when(modelMapper.map(userModel, UserModel.class)).thenReturn(userModel1);
        utils.sendEmailNow(emailModel);
        userService.sendMailToInvitedUser(UserStatus.INVITED);
        verify(userRepository, times(1)).findByUserStatusAndSoftDeleteIsFalse(UserStatus.INVITED);
    }

    @Test
    void testUserChartApi() throws InvocationTargetException, IllegalAccessException {
        double month = 0.0;
        month(1.0);
        month(2.0);
        month(3.0);
        month(4.0);
        month(5.0);
        month(6.0);
        month(7.0);
        month(8.0);
        month(9.0);
        month(10.0);
        month(11.0);
        month(12.0);
        int totalCount = 10;
        var userDateDetails = List.of(UserDateDetails.builder().userIds(Collections.singleton(id)).month(month).year(year).build());
        var monthAndYear = MonthAndYear.builder().title(Collections.singleton(title)).totalCount(totalCount).userDateDetails(userDateDetails).build();
        when(userRepository.userChartApi(year)).thenReturn(userDateDetails);
        userService.userChartApi(year);
        Assertions.assertEquals(userDateDetails, userService.userChartApi(year));
    }

    @Test
    void testGetUserBookDetailsByPagination() throws JSONException, InvocationTargetException, IllegalAccessException {
        FilterSortRequest.SortRequest<UserDataSortBy> sort = new FilterSortRequest.SortRequest<>();
        sort.setSortBy(UserDataSortBy.FULL_NAME);
        sort.setOrderBy(Sort.Direction.ASC);
        Pagination pagination = new Pagination();
        pagination.setPage(1);
        pagination.setLimit(10);
        PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getLimit());
        /*var userBookDetails = UserBookDetails.builder().bookName(bookName).userId(userId).bookId(bookId).fullName(fullName).build();*/
        var userIdsRequest = UserIdsRequest.builder().userId(Collections.singleton(id)).build();
        userService.getUserBookDetailsByPagination(userIdsRequest, sort, pageRequest);
        /*Assertions.assertEquals(userBookDetails,userService.getUserBookDetailsByPagination(userIdsRequest,sort,pageRequest));*/
    }

    @Test
    void testGetUserListByPagination() throws InvocationTargetException, IllegalAccessException {
        FilterSortRequest.SortRequest<UserSortBy> sort = new FilterSortRequest.SortRequest<>();
        sort.setSortBy(UserSortBy.EMAIL);
        sort.setOrderBy(Sort.Direction.ASC);
        Pagination pagination = new Pagination();
        pagination.setLimit(10);
        pagination.setPage(1);
        PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getLimit());
        UserFilter userFilter = new UserFilter();
        userFilter.setRole(Role.ADMIN);
        Page<UserModel> userModels = null;
        var userModel = List.of(UserModel.builder().id(id).userStatus(UserStatus.ACTIVE).build());
        Page<UserModel> page = new PageImpl<>(userModel);
        when(userRepository.findAllUserByFilterAndSortAndPage(userFilter, sort, pageRequest)).thenReturn(page);
        userService.getUserListByPagination();
        Assertions.assertEquals(userModel, userService.getUserListByPagination());
    }

    @Test
    void testGetUserResultBySemester() {
        var result = List.of(Result.builder().semester(semester).spi(spi).year(year).build());
        var userResult = UserResult.builder().userIds(Collections.singleton(id)).semester(Collections.singleton(semester)).build();
        var userResultResponse = List.of(UserResultResponse.builder().results(result).fullName(fullName).build());
        when(userRepository.getUserResultBySemester(userResult)).thenReturn(userResultResponse);
        userService.getUserResultBySemester(userResult);
        Assertions.assertEquals(userResultResponse, userService.getUserResultBySemester(userResult));
    }

    @Test
    void testGetUserResultByMinMaxSem() {
        var userIdsRequest = UserIdsRequest.builder().userId(Collections.singleton(id)).build();
        var userMinMaxMarkSemResponse = List.of(UserMinMaxMarkSemResponse.builder().id(id).fullName(fullName).build());
        when(userRepository.getUserResultByMinMaxMark(userIdsRequest)).thenReturn(userMinMaxMarkSemResponse);
        userService.getUserResultByMinMaxSem(userIdsRequest);
        Assertions.assertEquals(userMinMaxMarkSemResponse, userService.getUserResultByMinMaxSem(userIdsRequest));
    }

    @Test
    void testDeleteUserResult() {
        var result = List.of(Result.builder().semester(semester).spi(spi).year(year).build());
        var userModel = UserModel.builder().id(id).results(result).fullName(fullName).build();
        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        userService.deleteUserResult(id, semester);
        verify(userRepository, times(1)).findByIdAndSoftDeleteIsFalse(id);
    }

    @Test
    void testGetUserResultByDate() {
        var userResultByDate = UserResultByDate.builder().date(String.valueOf(date)).build();
        var result = List.of(Result.builder().year(year).semester(semester).spi(spi).build());
        var userResultByDateResponse = List.of(UserResultByDateRespose.builder().result(result).id(id).build());
        when(userRepository.getUserResultByDate(userResultByDate)).thenReturn(userResultByDateResponse);
        userService.getUserResultByDate(userResultByDate);
        Assertions.assertEquals(userResultByDateResponse, userService.getUserResultByDate(userResultByDate));
    }

    @Test
    void testUpdateUserResult() throws InvocationTargetException, IllegalAccessException {
        var resultUpdate = Resultupdate.builder().spi(spi).year(year).build();
        var userModel = UserModel.builder().userName(userName).id(id).build();
        var userResponse = UserResponse.builder().userName(userName).id(id).build();
        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        nullAwareBeanUtilsBean.copyProperties(userResponse, userModel);
        userService.updateUserResult(id, semester, resultUpdate);
        Assertions.assertEquals(userResponse, userService.updateUserResult(id, semester, resultUpdate));
    }

    @Test
    void testGetUserResultByStatus() {
        String status = String.valueOf(UserStatus.ACTIVE);
        var userIdsRequest = UserIdsRequest.builder().userId(Collections.singleton(id)).build();
        var result = List.of(Result.builder().year(year).spi(spi).semester(semester).status(status).build());
        var userResultByStatus = List.of(UserResultByStatus.builder().results(result).id(id).fullName(fullName).build());
        when(userRepository.getUserResultByStatus(userIdsRequest)).thenReturn(userResultByStatus);
        userService.getUserResultByStatus(userIdsRequest);
        Assertions.assertEquals(userResultByStatus, userService.getUserResultByStatus(userIdsRequest));
    }

    @Test
    void testGetUserResultStatusByPagination() throws InvocationTargetException, IllegalAccessException {
        FilterSortRequest.SortRequest<UserSortBy> sort = new FilterSortRequest.SortRequest<>();
        sort.setSortBy(UserSortBy.EMAIL);
        sort.setOrderBy(Sort.Direction.ASC);
        Pagination pagination = new Pagination();
        pagination.setPage(1);
        pagination.setLimit(10);
        PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getLimit());
        var result = List.of(Result.builder().year(year).status(status).spi(spi).semester(semester).build());
        var userIdsRequest = UserIdsRequest.builder().userId(Collections.singleton(id)).build();
        var userResultByStatus = List.of(UserResultByStatus.builder().result(result).id(id).fullName(fullName).build());
        Page<UserResultByStatus> page = new PageImpl<>(userResultByStatus);
        when(userRepository.findUserResultStatusByFilterAndSortAndPage(userIdsRequest, sort, pageRequest)).thenReturn(page);
        userService.getUserResultStatusByPagination(userIdsRequest, sort, pageRequest);
        Assertions.assertEquals(page, userService.getUserResultStatusByPagination(userIdsRequest, sort, pageRequest));
    }

    @Test
    void testResultDetailByEmail() throws InvocationTargetException, IllegalAccessException {
        var result = List.of(Result.builder().spi(spi).semester(semester).year(year).build());
        var userModel = UserModel.builder().id(id).results(result).cgpi(cgpi).build();
        var adminConfiguration = AdminConfiguration.builder().from(from).password(password).build();
        var emailModel = EmailModel.builder().to(to).message(message).subject(subject).cc(Collections.singleton(cc)).build();
        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        when(adminService.getConfigurationDetails()).thenReturn(adminConfiguration);
        utils.sendEmailNow(emailModel);
        utils.generateReportMessage(result, cgpi);
        userService.resultDetailByEmail(id);
        verify(userRepository, times(1)).findByIdAndSoftDeleteIsFalse(id);
    }
}
