package com.example.authmoduls.helper;

import com.example.authmoduls.auth.decorator.Result;
import com.example.authmoduls.auth.enums.UserStatus;
import com.example.authmoduls.auth.model.Address;
import com.example.authmoduls.auth.model.UserModel;
import com.example.authmoduls.common.enums.Role;
import com.example.authmoduls.common.utils.PasswordUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserHelper {

    List<UserModel> userModels = new ArrayList<>();
    public List<UserModel> userHelper(){
        UserModel userModel = new UserModel();
        userModel.setFullName("Pate Savan Kiritbhai");
        userModel.setFirstName("Pate");
        userModel.setMiddleName("Savan");
        userModel.setLastName("Kiritbhai");
        userModel.setAge(22);
        userModel.setUserName("Sp3112");
        userModel.setPassWord(PasswordUtils.encryptPassword("sp3112"));
        userModel.setMobileNo("9081738141");
        userModel.setEmail("savan9045@gmail.com");
        userModel.setNewPassWord("sp1234");
        userModel.setConfirmPassWord("sp1234");
        userModel.setAddress((List<Address>) new Address("f-7 neelDip flat","guruKulRoad","memNagar","ahmedabad","gujarat","380052"));
        userModel.setRole(Role.ADMIN);
        userModel.setOtp("123456");
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModels.add(userModel);
        return userModels;
    }



}
