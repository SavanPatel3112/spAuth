package com.example.authmoduls.helper;

import com.example.authmoduls.ar.auth.decorator.LoginAddRequest;
import com.example.authmoduls.ar.auth.decorator.LoginResponse;
import com.example.authmoduls.ar.auth.decorator.LoginTokenResponse;
import com.example.authmoduls.ar.auth.decorator.ShoppingListLog;
import com.example.authmoduls.ar.auth.model.Gender;
import com.example.authmoduls.ar.auth.model.Login;
import com.example.authmoduls.ar.auth.model.RecipeIngredient;
import com.example.authmoduls.auth.model.Accesss;
import com.example.authmoduls.common.model.AdminConfiguration;
import com.example.authmoduls.common.utils.JwtTokenUtil;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class LoginServiceTestDataGenerator {

    private static final String id = "6398478a1311382b51c49584";
    private static final String recipeId = "639b03d1665fa56d3ec3ca95";
    private static final String loginId = "6398478a1311382b51c49584";
    private static final String itemName = "Item";
    private static final String firstName = "Savan";
    private static final String middleName = "Kiritbhai";
    private static final String lastName = "Patel";
    private static final String fullName = "Savan Kiritbhai Patel ";
    private static final String email = "savan9045@gmail.com";
    private static final Accesss accesss = Accesss.USER;
    private static final Gender gender = Gender.MALE;
    private static final String passWord = ("$2a$12$WF3kZGAv0b0W/B/mCwc4h.2XcMJom/uKwTtB9fGLMyP/s8/t3Sihu");
    private static final String confirmPassword = "$2a$12$WF3kZGAv0b0W/B/mCwc4h.2XcMJom/uKwTtB9fGLMyP/s8/t3Sihu";
    private static final String host = "smtp.office365.com";
    private static final String port = "587";
    private static final String otp = "123456";
    private static final String from = "savan.p@techroversolutions.com";
    private static final String nameRegex = "^[0-9#$@!%&*?.-_=]{1,15}$";
    private static final String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,15}$";
    private static final String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String requiredEmailItems = "@";
    private static JwtTokenUtil jwtTokenUtil;

    public static Login login(){
       return Login.builder()
                .firstName(firstName)
                .middleName(middleName)
                .lastName(lastName)
                .passWord(passWord)
                .email(email)
                .gender(gender)
               .fullName(fullName)
                .accesss(accesss)
                .id(id)
                .softDelete(false)
                .build();
    }

    public static List<Login> loginList(){
        return List.of(Login.builder()
                .firstName(firstName)
                .middleName(middleName)
                .lastName(lastName)
                .passWord(passWord)
                .email(email)
                .gender(gender)
                .fullName(fullName)
                .accesss(accesss)
                .id(id)
                .softDelete(false)
                .build());
    }

    public static LoginResponse loginResponse(){
        return LoginResponse.builder()
                .id(id)
                .fullName(fullName)
                .firstName(firstName)
                .middleName(middleName)
                .lastName(lastName)
                .email(email)
                .accesss(accesss)
                .gender(gender)
                .passWord(passWord)
                .build();
    }

    public static List<LoginResponse> loginResponseList(){
        return List.of(LoginResponse.builder()
                .id(id)
                .fullName(fullName)
                .firstName(firstName)
                .middleName(middleName)
                .lastName(lastName)
                .email(email)
                .accesss(accesss)
                .gender(gender)
                .passWord(passWord)
                .build());
    }

    public static LoginAddRequest loginAddRequest(){
        return LoginAddRequest.builder()
                .firstName(firstName)
                .middleName(middleName)
                .lastName(lastName)
                .email(email)
                .passWord(passWord)
                .confirmPassword(confirmPassword)
                .build();
    }

    public static AdminConfiguration adminConfiguration(){
        return AdminConfiguration.builder()
                .id(id)
                .from(from)
                .host(host)
                .port(port)
                .passwordRegex(passwordRegex)
                .emailRegex(emailRegex)
                .nameRegex(nameRegex)
                .requiredEmailItems(Collections.singleton(requiredEmailItems))
                .build();
    }

    public static LoginTokenResponse loginTokenResponse(){
        return LoginTokenResponse.builder()
                .otp(otp)
                .accesss(accesss)
                .token("token")
                .build();
    }

    public static ShoppingListLog shoppingListLogs(){
        return ShoppingListLog.builder()
                .recipeId(recipeId)
                .loginId(loginId)
                .itemName(itemName)
                .ingredients(recipeIngredients())
                .softDelete(true)
                .date(new Date())
                .build();
    }

    public static List<ShoppingListLog> shoppingListLog(){
        return List.of(ShoppingListLog.builder()
                .recipeId(recipeId)
                .loginId(loginId)
                .itemName(itemName)
                .ingredients(recipeIngredients())
                .softDelete(false)
                .date(new Date())
                .build());
    }

    public static List<RecipeIngredient> recipeIngredients(){
        return List.of(RecipeIngredient.builder()
                .ingredientName("ingredientName")
                .quantity(5)
                .build());
    }

}
