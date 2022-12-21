package com.example.authmoduls.ar.auth.service;

import com.amazonaws.services.athena.model.InvalidRequestException;
import com.example.authmoduls.ar.auth.decorator.*;
import com.example.authmoduls.ar.auth.model.Gender;
import com.example.authmoduls.ar.auth.model.Login;
import com.example.authmoduls.ar.auth.repository.LoginRepository;
import com.example.authmoduls.ar.auth.repository.ShoppingListLogRepository;
import com.example.authmoduls.auth.model.Accesss;
import com.example.authmoduls.common.constant.MessageConstant;
import com.example.authmoduls.common.decorator.FilterSortRequest;
import com.example.authmoduls.common.decorator.NullAwareBeanUtilsBean;
import com.example.authmoduls.common.enums.PasswordEncryptionType;
import com.example.authmoduls.common.exception.AlreadyExistException;
import com.example.authmoduls.common.exception.InvaildRequestException;
import com.example.authmoduls.common.exception.NotFoundException;
import com.example.authmoduls.common.model.AdminConfiguration;
import com.example.authmoduls.common.model.EmailModel;
import com.example.authmoduls.common.model.JWTUser;
import com.example.authmoduls.common.service.AdminConfigurationService;
import com.example.authmoduls.common.utils.JwtTokenUtil;
import com.example.authmoduls.common.utils.PasswordUtils;
import com.example.authmoduls.common.utils.Utils;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService{

    @Autowired
    RecipeService recipeService;

    @Autowired
    ShoppingListLogRepository shoppingListLogRepository;


    private final LoginRepository loginRepository;
    private final NullAwareBeanUtilsBean nullAwareBeanUtilsBean;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordUtils passwordUtils;
    private final AdminConfigurationService adminService;
    private final Utils utils;
    private final ModelMapper modelMapper;


    public LoginServiceImpl(LoginRepository loginRepository, NullAwareBeanUtilsBean nullAwareBeanUtilsBean,
                            JwtTokenUtil jwtTokenUtil, PasswordUtils passwordUtils, AdminConfigurationService adminService,
                            Utils utils, ModelMapper modelMapper ) {
        this.loginRepository = loginRepository;
        this.nullAwareBeanUtilsBean = nullAwareBeanUtilsBean;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordUtils = passwordUtils;
        this.adminService = adminService;
        this.utils = utils;
        this.modelMapper = modelMapper;

    }

    @Override
    public LoginResponse addOrUpdateUsers(LoginAddRequest loginAddRequest, String id, Accesss accesss, Gender gender) throws InvocationTargetException, IllegalAccessException, NoSuchAlgorithmException {
        if (id != null) {
            Login login = getLoginModel(id);
            login.setFullName();
            nullAwareBeanUtilsBean.copyProperties(login, loginAddRequest);
            loginRepository.save(login);
            return modelMapper.map(login, LoginResponse.class);
        } else {
            if (accesss == null)//check user role
                throw new InvaildRequestException(MessageConstant.ROLE_NOT_FOUND);
        }
        checkUserDetails(loginAddRequest);
        Login login = new Login();
        nullAwareBeanUtilsBean.copyProperties(login,loginAddRequest);
        login.setFullName();
        login.setAccesss(accesss);
        login.setGender(gender);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setPassWord(login.getPassWord());
        if (login.getPassWord() != null) {
            String password = PasswordUtils.encryptPassword(Utils.decodeBase64(login.getPassWord()));
            login.setPassWord(password);
            loginResponse.setPassWord(password);
            nullAwareBeanUtilsBean.copyProperties(loginResponse, login);
        }
        loginRepository.save(login);
        nullAwareBeanUtilsBean.copyProperties(loginResponse,login);
        return loginResponse;
    }

    @Override
    public LoginResponse getUser(String id) throws InvocationTargetException, IllegalAccessException {
        Login login = getLoginModel(id);
        LoginResponse loginResponse = new LoginResponse();
        modelMapper.map(login, loginResponse);
        /*nullAwareBeanUtilsBean.copyProperties(loginResponse,login);*/
        return loginResponse;
    }

    @Override
    public void deleteUser(String id) {
        Login login = getLoginModel(id);
        login.setSoftDelete(true);
        loginRepository.save(login);
    }

    @Override
    public List<LoginResponse> getAllUser() throws InvocationTargetException, IllegalAccessException {
        List<Login> logins = loginRepository.findAllBySoftDeleteFalse();
        List<LoginResponse> loginResponses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(logins)) {
            for (Login login : logins) {
                LoginResponse loginResponse = new LoginResponse();
                /*nullAwareBeanUtilsBean.copyProperties(loginResponse, login);*/
                modelMapper.map(login,loginResponse);
                loginResponses.add(loginResponse);
            }
        }
        return loginResponses;
    }

    @Override
    public Page<Login> getAllUserByFilterAndSortAndPage(LoginFilter loginFilter, FilterSortRequest.SortRequest<LoginSortBy> sort, PageRequest pagination) {
        return loginRepository.findAllUserByFilterAndSortAndPage(loginFilter, sort, pagination);
    }

    @Override
    public LoginResponse getToken(String id) throws InvocationTargetException, IllegalAccessException {
        Login login = getLoginModel(id);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccesss(login.getAccesss());
        JWTUser jwtUser = new JWTUser(id, Collections.singletonList(loginResponse.getAccesss().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        /*nullAwareBeanUtilsBean.copyProperties(loginResponse, login);*/
        modelMapper.map(loginResponse,login);
        loginResponse.setToken(token);
        return loginResponse;
    }

    @Override
    public LoginTokenResponse userLogin(LoginRequest loginRequest) throws NoSuchAlgorithmException, InvocationTargetException, IllegalAccessException {
        Login login = getUserEmail(loginRequest.getEmail());
        String otp = generateOtp();
        LoginTokenResponse loginTokenResponse = new LoginTokenResponse();
        loginTokenResponse.setAccesss(login.getAccesss());
        JWTUser jwtUser = new JWTUser(login.getId(), Collections.singletonList(loginTokenResponse.getAccesss().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        modelMapper.map(loginTokenResponse,login);
        loginTokenResponse.setToken(token);
        /*AdminConfiguration adminConfiguration = adminService.getConfigurationDetails();*/
        boolean passwords = passwordUtils.isPasswordAuthenticated(Utils.decodeBase64(loginRequest.getPassword()), login.getPassWord(), PasswordEncryptionType.BCRYPT);
        if (passwords) {
          /*  EmailModel emailModel = new EmailModel();
            emailModel.setMessage(otp);
            emailModel.setTo(login.getEmail());
            emailModel.setCc(adminConfiguration.getTechAdmins());
            emailModel.setSubject("RecipeBook OTP Verification ");
            utils.sendEmailNow(emailModel);*/
            login.setOtp(otp);
            login.setLogin(true);
            Date date = new Date();
            login.setLoginTime(date);
            loginTokenResponse.setOtp(otp);
            loginRepository.save(login);
        } else {
            throw new NotFoundException(MessageConstant.PASSWORD_NOT_MATCHED);
        }
        return loginTokenResponse;
    }

    @Override
    public String getEncryptPassword(String id) throws InvocationTargetException, IllegalAccessException {
        Login login = getLoginModel(id);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setPassWord(login.getPassWord());
        if (login.getPassWord() != null) {
            String password = PasswordUtils.encryptPassword(login.getPassWord());
            login.setPassWord(password);
            loginResponse.setPassWord(password);
            loginRepository.save(login);
            String passwords = loginResponse.getPassWord();
            /*nullAwareBeanUtilsBean.copyProperties(loginResponse, login);*/
            modelMapper.map(loginResponse,login);
            return passwords;
        } else {
            throw new NotFoundException(MessageConstant.PASSWORD_EMPTY);
        }
    }

    @Override
    public LoginResponse getValidityOfToken(String token) throws InvocationTargetException, IllegalAccessException {
        LoginResponse loginResponse = new LoginResponse();
        String validateToken = getIdFromToken(token);
        Login login = getLoginModel(validateToken);
        loginResponse.setId(validateToken);
        String tokenId = loginResponse.getId();
        JWTUser jwtUser = new JWTUser(tokenId,new ArrayList<>());
        boolean getValidate = jwtTokenUtil.validateToken(token, jwtUser);
        if (getValidate) {
            /*nullAwareBeanUtilsBean.copyProperties(loginResponse , login );*/
            modelMapper.map(loginResponse,login);
            return loginResponse;
        } else {
            throw new NotFoundException(MessageConstant.TOKEN_NOT_VAILD);
        }
    }

    @Override
    public String getIdFromToken(String token) {
        String id = jwtTokenUtil.getUserIdFromToken(token);
        boolean exists = loginRepository.existsByIdAndSoftDeleteFalse(id);
        if (exists) {
            return id;
        } else {
            throw new InvaildRequestException(MessageConstant.INVAILD_TOKEN);
        }
    }

    @Override
    public void logOut(String id) {
        Login login = getLoginModel(id);
        login.setLogin(false);
        login.setLogoutTime(new Date());
        loginRepository.save(login);
        List<ShoppingListLog> shoppingListLog = recipeService.getShoppingList(id);
        for (ShoppingListLog listLog : shoppingListLog) {
            listLog.setSoftDelete(true);
            shoppingListLogRepository.save(listLog);
        }
    }

    @VisibleForTesting
    public String generateOtp() {
        Random random = new Random();
        int number = random.nextInt(999999);
        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }
    @VisibleForTesting
    public String token(String id,Accesss accesss){
        JWTUser jwtUser = new JWTUser(id, Collections.singletonList(accesss.toString()));
        return jwtTokenUtil.generateToken(jwtUser);
    }

    public void checkUserDetails(LoginAddRequest loginAddRequest) throws InvocationTargetException, IllegalAccessException {
        AdminConfiguration adminConfiguration = adminService.getConfigurationDetails();
        if ((StringUtils.isEmpty(loginAddRequest.getFirstName()) || (loginAddRequest.getFirstName().matches(adminConfiguration.getNameRegex())))) {
            throw new NotFoundException(MessageConstant.FIRSTNAME_NOT_EMPTY);
        }
        if ((StringUtils.isEmpty(loginAddRequest.getMiddleName()) || (loginAddRequest.getMiddleName().matches(adminConfiguration.getNameRegex())))) {
            throw new NotFoundException(MessageConstant.MIDDLENAME_NOT_EMPTY);
        }
        if ((StringUtils.isEmpty(loginAddRequest.getLastName()) || (loginAddRequest.getLastName().matches(adminConfiguration.getNameRegex())))) {
            throw new NotFoundException(MessageConstant.LASTNAME_NOT_EMPTY);
        }
        if (StringUtils.isEmpty(loginAddRequest.getEmail())) {
            throw new NotFoundException(MessageConstant.EMAIL_NOT_FOUND);
        }
        if (loginRepository.existsByEmailAndSoftDeleteFalse(loginAddRequest.getEmail())) {
            throw new NotFoundException(MessageConstant.EMAIL_NAME_EXISTS);
        }
        if (StringUtils.isEmpty(loginAddRequest.getEmail()) && CollectionUtils.isEmpty(adminConfiguration.getRequiredEmailItems())) {
            throw new NotFoundException(MessageConstant.EMAIL_EMPTY);
        }
        if (!loginAddRequest.getEmail().matches(adminConfiguration.getRegex())) {
            throw new NotFoundException(MessageConstant.EMAIL_FORMAT_NOT_VALID);
        }
        if (!Utils.decodeBase64(loginAddRequest.getPassWord()).matches(adminConfiguration.getPasswordRegex())) {
            throw new NotFoundException(MessageConstant.INVAILD_PASSWORD);
        }
        if (!Objects.equals(loginAddRequest.getPassWord(), loginAddRequest.getConfirmPassword())){
            throw new NotFoundException(MessageConstant.PASSWORD_INVALID);
        }
    }

    public Login getLoginModel(String id) {
        return loginRepository.findByIdAndSoftDeleteIsFalse(id).orElseThrow(() -> new NotFoundException(MessageConstant.USER_ID_NOT_FOUND));
    }

    @Override
    public void otpVerification(OtpVerification otp) {
        Login login = getUserEmail(otp.getEmail());
        if (StringUtils.isEmpty(login.getEmail())){
            throw new NotFoundException(MessageConstant.EMAIL_NOT_FOUND);
        }
        boolean exists = loginRepository.existsByEmailAndOtpAndSoftDeleteFalse(otp.getEmail(),otp.getOtp());
        if (!exists){
            throw new NotFoundException(MessageConstant.INVAILD_OTP);
        }
    }

    private Login getUserEmail(String email) {
        return loginRepository.findByEmailAndSoftDeleteIsFalse(email).orElseThrow(()-> new NotFoundException(MessageConstant.EMAIL_NOT_FOUND));
    }
}
