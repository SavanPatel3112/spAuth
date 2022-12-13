package com.example.authmoduls.auth.controller;

import com.example.authmoduls.ar.auth.decorator.ShoppingAddRequest;
import com.example.authmoduls.ar.auth.decorator.ShoppingResponse;
import com.example.authmoduls.ar.auth.service.ShoppingService;
import com.example.authmoduls.auth.model.Accesss;
import com.example.authmoduls.common.decorator.DataResponse;
import com.example.authmoduls.common.decorator.Response;
import com.example.authmoduls.common.decorator.TokenResponse;
import com.example.authmoduls.common.enums.Role;
import com.example.authmoduls.common.utils.Access;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("/shopping")
@CrossOrigin("*")
public class ShoppingController{

    @Autowired
    ShoppingService shoppingService;

    @Access(level = {Accesss.ANONYMOUS})
    @RequestMapping(name = "addOrUpdateShopping" , value = "/addOrUpdateShopping" , method = RequestMethod.POST)
    public DataResponse<ShoppingResponse> addOrUpdateShopping(@RequestBody ShoppingAddRequest shoppingAddRequest, Accesss accesss, @RequestParam(required = false) String id) throws InvocationTargetException, IllegalAccessException {
        DataResponse<ShoppingResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(shoppingService.addOrUpdateShopping(shoppingAddRequest, accesss,id));
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }

    @Access(level = {Accesss.USER})
    @RequestMapping(name = "getShoppingList" , value = "/get/id" , method = RequestMethod.GET)
    public DataResponse<ShoppingResponse> getShoppingList(@RequestParam String id) throws InvocationTargetException, IllegalAccessException {
        DataResponse<ShoppingResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(shoppingService.getShoppingList(id));
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }

    @Access(level = {Accesss.USER})
    @RequestMapping(name = "deleteShoppingList", value = "/delete/id", method = RequestMethod.DELETE)
    public DataResponse<Object> deleteShoppingList(@RequestParam String id) {
        DataResponse<Object> dataResponse = new DataResponse<>();
        shoppingService.deleteShoppingList(id);
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }

    @SneakyThrows
    @Access(level = Accesss.ANONYMOUS)
    @RequestMapping(name = "getToken", value = "/generateToken", method = RequestMethod.GET)
    public TokenResponse<ShoppingResponse> getToken(@RequestParam String id) {
        TokenResponse<ShoppingResponse> tokenResponse = new TokenResponse<>();
        ShoppingResponse shoppingResponse = shoppingService.getToken(id);
        tokenResponse.setData(shoppingResponse);
        tokenResponse.setStatus(Response.getOkResponse());
        tokenResponse.setToken(shoppingResponse.getToken());
        return tokenResponse;
    }

}
