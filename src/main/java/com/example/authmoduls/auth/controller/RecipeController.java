package com.example.authmoduls.auth.controller;

import com.example.authmoduls.ar.auth.decorator.RecipeAddRequest;
import com.example.authmoduls.ar.auth.decorator.RecipeResponse;
import com.example.authmoduls.ar.auth.decorator.ShoppingListLog;
import com.example.authmoduls.ar.auth.service.RecipeService;
import com.example.authmoduls.auth.model.Accesss;
import com.example.authmoduls.common.decorator.*;
import com.example.authmoduls.common.utils.Access;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/recipe")
@CrossOrigin("*")
@Slf4j
public class RecipeController {
    @Autowired
    RecipeService recipeService;
    @Autowired
    RequestSession requestSession;
    @Access(level = {Accesss.ANONYMOUS})
    @RequestMapping(name = "addOrUpdateRecipe" , value = "/addOrUpdateRecipe" , method = RequestMethod.POST)
    public DataResponse<RecipeResponse> addOrUpdateRecipe (@RequestBody RecipeAddRequest recipeAddRequest, Accesss accesss, @RequestParam(required = false)String id) throws InvocationTargetException, IllegalAccessException {
        DataResponse<RecipeResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(recipeService.addOrUpdateRecipe(recipeAddRequest, id , accesss));
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }
    @Access(level = {Accesss.ANONYMOUS})
    @RequestMapping(name = "getAllRecipe" , value = "/getAllRecipe" , method = RequestMethod.GET)
    public ListResponse<RecipeResponse> getAllRecipe() throws InvocationTargetException, IllegalAccessException {
        ListResponse<RecipeResponse> listResponse = new ListResponse<>();
        listResponse.setData(recipeService.getAllRecipe());
        listResponse.setStatus(Response.getOkResponse());
        return listResponse;
    }

    @Access(level = {Accesss.ANONYMOUS})
    @RequestMapping(name = "getRecipe" , value = "/get/id" , method = RequestMethod.GET)
    public DataResponse<RecipeResponse> getRecipe (@RequestParam String id) throws InvocationTargetException, IllegalAccessException {
        DataResponse<RecipeResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(recipeService.getRecipe(id));
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }
    @Access(level = {Accesss.ANONYMOUS})
    @RequestMapping(name = "deleteRecipe", value = "/delete/id", method = RequestMethod.DELETE)
    public DataResponse<Object> deleteUser (@RequestParam String id) {
        DataResponse<Object> dataResponse = new DataResponse<>();
        recipeService.deleteRecipe(id);
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }
    @Access(level = {Accesss.ANONYMOUS})
    @RequestMapping(name = "recipeUpdate" , value = "update/id" , method = RequestMethod.POST)
    public DataResponse<Object> recipeUpdate (@RequestParam String id , @RequestParam Accesss accesss , @RequestBody RecipeAddRequest recipeAddRequest) throws InvocationTargetException, IllegalAccessException {
        DataResponse<Object>  dataResponse = new DataResponse<>();
        recipeService.recipeUpdate(id, accesss,recipeAddRequest);
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }

    @SneakyThrows
    @Access(level = {Accesss.USER})
    @RequestMapping(name = "shoppingList ", value = "shoppingList", method = RequestMethod.POST)
    public DataResponse<Object> shoppingList(@RequestParam String id) {
        DataResponse<Object> dataResponse = new DataResponse<>();
        String loginID = requestSession.getJwtUser().getId();
        recipeService.shoppingList(id, loginID);
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }

    @SneakyThrows
    @Access(level = Accesss.USER)
    @RequestMapping(name = "generatePdfFile" , value = "/export-to-pdf" , method = RequestMethod.GET)
    public void generatePdfFile(HttpServletResponse response){
        String loginId = requestSession.getJwtUser().getId();
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=RecipeIngredient" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        List<ShoppingListLog> shoppingListLogs = recipeService.getRecipeList(loginId);
        PdfGenerator generator = new PdfGenerator();
        log.info("generator:{}",generator);
        generator.generate(shoppingListLogs, response);
    }

}
