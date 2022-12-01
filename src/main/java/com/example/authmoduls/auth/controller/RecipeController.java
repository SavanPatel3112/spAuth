package com.example.authmoduls.auth.controller;

import com.example.authmoduls.ar.auth.decorator.RecipeAddRequest;
import com.example.authmoduls.ar.auth.decorator.RecipeResponse;
import com.example.authmoduls.ar.auth.service.RecipeService;
import com.example.authmoduls.common.decorator.DataResponse;
import com.example.authmoduls.common.decorator.ListResponse;
import com.example.authmoduls.common.decorator.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("/recipe")
public class RecipeController {
    @Autowired
    RecipeService recipeService;
    @RequestMapping(name = "addOrUpdateRecipe" , value = "/addOrUpdateRecipe" , method = RequestMethod.POST)
    public DataResponse<RecipeResponse> addOrUpdateRecipe (@RequestBody RecipeAddRequest recipeAddRequest , @RequestParam(required = false)String id) throws InvocationTargetException, IllegalAccessException {
        DataResponse<RecipeResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(recipeService.addOrUpdateRecipe(recipeAddRequest,id));
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }
    @RequestMapping(name = "getAllRecipe" , value = "/getAllRecipe" , method = RequestMethod.GET)
    public ListResponse<RecipeResponse> getAllRecipe() throws InvocationTargetException, IllegalAccessException {
        ListResponse<RecipeResponse> listResponse = new ListResponse<>();
        listResponse.setData(recipeService.getAllRecipe());
        listResponse.setStatus(Response.getOkResponse());
        return listResponse;
    }
    @RequestMapping(name = "getRecipe" , value = "/get/id" , method = RequestMethod.GET)
    public DataResponse<RecipeResponse> getRecipe (@RequestParam String id) throws InvocationTargetException, IllegalAccessException {
        DataResponse<RecipeResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(recipeService.getRecipe(id));
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }
    @RequestMapping(name = "deleteRecipe", value = "/delete/id", method = RequestMethod.DELETE)
    public DataResponse<Object> deleteUser (@RequestParam String id) {
        DataResponse<Object> dataResponse = new DataResponse<>();
        recipeService.deleteRecipe(id);
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }
}
