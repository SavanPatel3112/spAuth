package com.example.authmoduls.helper;

import com.example.authmoduls.ar.auth.decorator.RecipeAddRequest;
import com.example.authmoduls.ar.auth.decorator.RecipeResponse;
import com.example.authmoduls.ar.auth.decorator.ShoppingListLog;
import com.example.authmoduls.ar.auth.model.Gender;
import com.example.authmoduls.ar.auth.model.Login;
import com.example.authmoduls.ar.auth.model.RecipeIngredient;
import com.example.authmoduls.ar.auth.model.RecipeModel;
import com.example.authmoduls.auth.model.Accesss;

import java.util.Date;
import java.util.List;

public class RecipeServiceTestDataGenerator {
    private static final String id = "639b03d1665fa56d3ec3ca95";
    private static final String recipeId = "639b03d1665fa56d3ec3ca95";
    private static final String loginId = "6398478a1311382b51c49584";
    private static final Accesss access = Accesss.USER;
    private static final String itemName = "Samosa";
    private static final String itemDescription = "Some mst dish";
    private static final String itemUrl = "url";
    private static final String firstName = "Savan";
    private static final String middleName = "Kiritbhai";
    private static final String lastName = "Patel";
    private static final String fullName = "Savan Kiritbhai Patel ";
    private static final String email = "savan9045@gmail.com";
    private static final String passWord = ("$2a$12$WF3kZGAv0b0W/B/mCwc4h.2XcMJom/uKwTtB9fGLMyP/s8/t3Sihu");

    public static List<RecipeIngredient> recipeIngredient(){
        return List.of(RecipeIngredient.builder()
                .ingredientName("ingredientName")
                .quantity(1)
                .build());
    }

    public static RecipeModel recipeModel(){
        return RecipeModel.builder()
                .id(id)
                .accesss(access)
                .itemName(itemName)
                .itemDescription(itemDescription)
                .itemUrl(itemUrl)
                .recipeIngredient(recipeIngredient())
                .build();
    }

    public static List<RecipeModel> recipeModelList(){
        return List.of(RecipeModel.builder()
                .id(id)
                .accesss(access)
                .itemName(itemName)
                .itemDescription(itemDescription)
                .itemUrl(itemUrl)
                .recipeIngredient(recipeIngredient())
                .build());
    }

    public static RecipeAddRequest recipeAddRequest(){
        return RecipeAddRequest.builder()
                .itemName(itemName)
                .itemDescription(itemDescription)
                .itemUrl(itemUrl)
                .recipeIngredient(recipeIngredient())
                .build();
    }

    public static RecipeResponse recipeResponse(){
        return RecipeResponse.builder()
                .id(id)
                .itemName(itemName)
                .itemDescription(itemDescription)
                .itemUrl(itemUrl)
                .recipeIngredient(recipeIngredient())
                .accesss(access)
                .softDelete(false)
                .build();
    }

    public static List<RecipeResponse> recipeResponseList(){
        return List.of(RecipeResponse.builder()
                .id(id)
                .itemName(itemName)
                .itemDescription(itemDescription)
                .itemUrl(itemUrl)
                .recipeIngredient(recipeIngredient())
                .accesss(access)
                .softDelete(false)
                .build());
    }

    public static Login login(){
        return Login.builder()
                .firstName(firstName)
                .middleName(middleName)
                .lastName(lastName)
                .passWord(passWord)
                .email(email)
                .gender(Gender.MALE)
                .fullName(fullName)
                .accesss(access)
                .id(id)
                .softDelete(false)
                .build();
    }

    public static ShoppingListLog shoppingListLog(){
        return ShoppingListLog.builder()
                .loginId(loginId)
                .recipeId(recipeId)
                .itemName(itemName)
                .ingredients(recipeIngredient())
                .softDelete(false)
                .date(new Date())
                .build();
    }




}
