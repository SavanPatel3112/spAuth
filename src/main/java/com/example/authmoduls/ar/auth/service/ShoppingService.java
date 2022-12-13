package com.example.authmoduls.ar.auth.service;

import com.example.authmoduls.ar.auth.decorator.ShoppingAddRequest;
import com.example.authmoduls.ar.auth.decorator.ShoppingResponse;
import com.example.authmoduls.auth.model.Accesss;
import com.example.authmoduls.common.enums.Role;

import java.lang.reflect.InvocationTargetException;

public interface ShoppingService {
    ShoppingResponse addOrUpdateShopping(ShoppingAddRequest shoppingAddRequest, Accesss accesss, String id) throws InvocationTargetException, IllegalAccessException;

    ShoppingResponse getShoppingList(String id) throws InvocationTargetException, IllegalAccessException;

    void deleteShoppingList(String id);

    ShoppingResponse getToken(String id) throws InvocationTargetException, IllegalAccessException;
}
