package com.example.authmoduls.auth.service.userService;

import com.example.authmoduls.auth.decorator.*;
import com.example.authmoduls.auth.decorator.bookDecorator.UserBookDetails;
import com.example.authmoduls.auth.enums.UserSortBy;
import com.example.authmoduls.auth.enums.UserStatus;
import com.example.authmoduls.auth.model.UserModel;
import com.example.authmoduls.common.decorator.FilterSortRequest;
import com.example.authmoduls.common.decorator.UserImportResponse;
import com.example.authmoduls.common.decorator.UserImportVerifyRequest;
import com.example.authmoduls.common.enums.Role;
import com.example.authmoduls.common.model.UserDataModel;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONException;
import org.quartz.SchedulerException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserService {
    UserResponse addOrUpdateUser(UserAddRequest userAddRequest, String id, Role role) throws InvocationTargetException, IllegalAccessException;

    List<UserResponse> getAllUser() throws InvocationTargetException, IllegalAccessException;

    UserModel getUserModel(String id);

    UserResponse getUser(String id) throws InvocationTargetException, IllegalAccessException;

    void deleteUser(String id);

    Page<UserModel> getAllUserByFilterAndSortAndPage(UserFilter filter, FilterSortRequest.SortRequest<UserSortBy> sort, PageRequest pagination) throws InvocationTargetException, IllegalAccessException;

    UserResponse getToken(String id) throws InvocationTargetException, IllegalAccessException;

    String getEncryptPassword(String id) throws InvocationTargetException, IllegalAccessException;

    UserResponse checkUserAuthentication(String email, String password) throws NoSuchAlgorithmException, InvocationTargetException, IllegalAccessException;

    String getIdFromToken(String token) throws InvocationTargetException, IllegalAccessException;

    UserResponse getValidityOfToken(String token) throws InvocationTargetException, IllegalAccessException;

    void userLogin(String email, String password) throws InvocationTargetException, IllegalAccessException, NoSuchAlgorithmException;

    UserResponse getOtp(String otp, String id) throws InvocationTargetException, IllegalAccessException;

    void forgotPassword(String email);

    void setPassword(String password, String confirmPassword, String id);

    void otpVerifications(String otp, String id) throws InvocationTargetException, IllegalAccessException;

    void changePassword(String password, String confirmPassword, String newPassword, String id) throws NoSuchAlgorithmException;

    void logOut(String id) throws InvocationTargetException, IllegalAccessException, SchedulerException;

    List<UserResponse> getUserByRole(UserFilter userFilter);

    UserResponse addResult(Result result, String id) throws InvocationTargetException, IllegalAccessException;

    List<UserDetailResponse> getUserResult(UserDetail userDetail) throws InvocationTargetException, IllegalAccessException;

    List<UserResultResponse> getUserResultBySemester(UserResult userResult);

    List<UserMinMaxMarkSemResponse> getUserResultByMinMaxSem(UserIdsRequest userIdsRequest);

    void deleteUserResult(String id, int semester);

    List<UserResultByDateRespose> getUserResultByDate(UserResultByDate userResultByDate);

    UserResponse updateUserResult(String id, int semester, Resultupdate result) throws InvocationTargetException, IllegalAccessException;

    List<UserResultByStatus> getUserResultByStatus(UserIdsRequest userIdsRequest);

    Page<UserResultByStatus> getUserResultStatusByPagination(UserIdsRequest userIdsRequest, FilterSortRequest.SortRequest<UserSortBy> sort, PageRequest pagination) throws InvocationTargetException, IllegalAccessException;

    Workbook getUserByExcel(UserFilter userFilter, FilterSortRequest.SortRequest<UserSortBy> sort, PageRequest pagination) throws InvocationTargetException, IllegalAccessException;

    void resultDetailByEmail(String id) throws InvocationTargetException, IllegalAccessException;

    void userUpdate(String id, Role role, UserAddRequest userAddRequest) throws InvocationTargetException, IllegalAccessException;

    void userDelete(String id, Role role) throws InvocationTargetException, IllegalAccessException;

    String uploadFile(MultipartFile multipartFile) throws IOException;

    UserImportResponse importUsers(MultipartFile file, String id) throws IOException, InvocationTargetException, IllegalAccessException;

    List<UserDataModel> importUsersVerify(UserImportVerifyRequest verifyRequest);

    List<UserResponse> importDataInUser(UserIdsRequest userIdsRequest) throws InvocationTargetException, IllegalAccessException;

    void deleteUserInXls(String id);

    void getUserPassword(String userName, String password, String confirmPassword) throws InvocationTargetException, IllegalAccessException;

    void sendMailToInvitedUser(UserStatus userStatus) throws InvocationTargetException, IllegalAccessException;

    void sendMessage(String id);

    MonthAndYear userChartApi(int year) throws InvocationTargetException, IllegalAccessException;

    Page<UserBookDetails> getUserBookDetailsByPagination(UserIdsRequest userIdsRequest, FilterSortRequest.SortRequest<UserDataSortBy> sort, PageRequest pagination) throws InvocationTargetException, IllegalAccessException, JSONException;

    List<UserModel> getUserListByPagination() throws InvocationTargetException, IllegalAccessException;

}
