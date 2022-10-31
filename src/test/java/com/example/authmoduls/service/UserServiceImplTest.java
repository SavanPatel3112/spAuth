package com.example.authmoduls.service;

import com.example.authmoduls.auth.decorator.UserAddRequest;
import com.example.authmoduls.auth.decorator.UserFilter;
import com.example.authmoduls.auth.decorator.UserResponse;
import com.example.authmoduls.auth.enums.UserSortBy;
import com.example.authmoduls.auth.model.Address;
import com.example.authmoduls.auth.model.UserModel;
import com.example.authmoduls.auth.repository.userRepository.UserRepository;
import com.example.authmoduls.auth.service.userService.UserService;
import com.example.authmoduls.common.decorator.FilterSortRequest;
import com.example.authmoduls.common.decorator.PageResponse;
import com.example.authmoduls.common.decorator.Pagination;
import com.example.authmoduls.common.model.JWTUser;
import com.example.authmoduls.common.utils.JwtTokenUtil;
import com.example.authmoduls.common.utils.PasswordUtils;
import com.example.authmoduls.helper.DataSetHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Slf4j
public class UserServiceImplTest {

    @Autowired
    DataSetHelper dataSetHelper;
    @Mock
    UserService userService;
    @Mock
    UserRepository userRepository;
    @Mock
    JwtTokenUtil jwtTokenUtil;
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    @BeforeEach
    public void getDataSetHelper() {
        dataSetHelper.cleanUp();
        dataSetHelper.init();
    }

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
    public void getAllUser() throws InvocationTargetException, IllegalAccessException {
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            when(userRepository.findAllBySoftDeleteFalse()).thenReturn(List.of(userModel));
            Assertions.assertEquals(userService.getAllUser(), new ArrayList<>());
            verify(userRepository, times(1)).findAllBySoftDeleteFalse();
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void addOrUpdateUser() throws InvocationTargetException, IllegalAccessException {
        try {
            UserModel userModel = dataSetHelper.getUserModel();
            UserAddRequest userAddRequest = new UserAddRequest();
            userAddRequest.setUserName("pk1402");
            userAddRequest.setEmail("pk@102.com");
            userAddRequest.setFirstName("Pinkesh");
            userAddRequest.setMiddleName("Kiritbhai");
            userAddRequest.setLastName("Patel");
            userAddRequest.setPassword("Pk@1402");
            userAddRequest.setAddress(new Address("f-7 neelDip flat","guruKulRoad","memNagar","ahmedabad","gujarat","380062"));
            userService.addOrUpdateUser(userAddRequest,userModel.getId(),userModel.getRole());
            verify(userRepository,times(1)).save(userModel);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
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
}
