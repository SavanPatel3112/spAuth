package com.example.authmoduls.auth.controller;

import com.example.authmoduls.ar.auth.decorator.RecipeAddRequest;
import com.example.authmoduls.ar.auth.decorator.RecipeResponse;
import com.example.authmoduls.ar.auth.service.RecipeService;
import com.example.authmoduls.common.decorator.DataResponse;
import com.example.authmoduls.common.decorator.ListResponse;
import com.example.authmoduls.common.decorator.Response;
import com.example.authmoduls.common.enums.Role;
import com.example.authmoduls.common.utils.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("/recipe")
@CrossOrigin("*")
public class RecipeController {
    @Autowired
    RecipeService recipeService;
    @Access(levels = {Role.ANONYMOUS})
    @RequestMapping(name = "addOrUpdateRecipe" , value = "/addOrUpdateRecipe" , method = RequestMethod.POST)
    public DataResponse<RecipeResponse> addOrUpdateRecipe (@RequestBody RecipeAddRequest recipeAddRequest, Role role, @RequestParam(required = false)String id) throws InvocationTargetException, IllegalAccessException {
        DataResponse<RecipeResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(recipeService.addOrUpdateRecipe(recipeAddRequest, id ,role));
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }
    @Access(levels = {Role.ANONYMOUS})
    @RequestMapping(name = "getAllRecipe" , value = "/getAllRecipe" , method = RequestMethod.GET)
    public ListResponse<RecipeResponse> getAllRecipe() throws InvocationTargetException, IllegalAccessException {
        ListResponse<RecipeResponse> listResponse = new ListResponse<>();
        listResponse.setData(recipeService.getAllRecipe());
        listResponse.setStatus(Response.getOkResponse());
        return listResponse;
    }

    @Access(levels = {Role.ANONYMOUS})
    @RequestMapping(name = "getRecipe" , value = "/get/id" , method = RequestMethod.GET)
    public DataResponse<RecipeResponse> getRecipe (@RequestParam String id) throws InvocationTargetException, IllegalAccessException {
        DataResponse<RecipeResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(recipeService.getRecipe(id));
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }
    @Access(levels = {Role.ANONYMOUS})
    @RequestMapping(name = "deleteRecipe", value = "/delete/id", method = RequestMethod.DELETE)
    public DataResponse<Object> deleteUser (@RequestParam String id) {
        DataResponse<Object> dataResponse = new DataResponse<>();
        recipeService.deleteRecipe(id);
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }
    @Access(levels = {Role.ANONYMOUS})
    @RequestMapping(name = "recipeUpdate" , value = "update/id" , method = RequestMethod.POST)
    public DataResponse<Object> recipeUpdate (@RequestParam String id , @RequestParam Role role , @RequestBody RecipeAddRequest recipeAddRequest) throws InvocationTargetException, IllegalAccessException {
        DataResponse<Object>  dataResponse = new DataResponse<>();
        recipeService.recipeUpdate(id,role,recipeAddRequest);
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }

}
