package com.example.authmoduls.ar.auth.service;

import com.example.authmoduls.ar.auth.constant.MessageConstant;
import com.example.authmoduls.ar.auth.decorator.*;
import com.example.authmoduls.ar.auth.model.Login;
import com.example.authmoduls.ar.auth.model.RecipeModel;
import com.example.authmoduls.ar.auth.repository.LoginRepository;
import com.example.authmoduls.ar.auth.repository.RecipeRepository;
import com.example.authmoduls.ar.auth.repository.ShoppingListLogRepository;
import com.example.authmoduls.auth.model.Accesss;
import com.example.authmoduls.common.decorator.FilterSortRequest;
import com.example.authmoduls.common.decorator.NullAwareBeanUtilsBean;
import com.example.authmoduls.common.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {
    private final NullAwareBeanUtilsBean nullAwareBeanUtilsBean;
    private final ModelMapper modelMapper;
    private final RecipeRepository recipeRepository;
    private final LoginService loginService;
    private final LoginRepository loginRepository;
    private final ShoppingListLogRepository shoppingListLogRepository;

    public RecipeServiceImpl(NullAwareBeanUtilsBean nullAwareBeanUtilsBean, ModelMapper modelMapper, RecipeRepository recipeRepository,LoginService loginService,LoginRepository loginRepository,ShoppingListLogRepository shoppingListLogRepository) {
        this.nullAwareBeanUtilsBean = nullAwareBeanUtilsBean;
        this.modelMapper = modelMapper;
        this.recipeRepository = recipeRepository;
        this.loginService = loginService;
        this.loginRepository = loginRepository;
        this.shoppingListLogRepository = shoppingListLogRepository;
    }

    @Override
    public RecipeResponse addOrUpdateRecipe(RecipeAddRequest recipeAddRequest, String id, Accesss accesss ) throws InvocationTargetException, IllegalAccessException {
        if (id != null) {
            RecipeModel recipeModel = getRecipeModel(id);
            nullAwareBeanUtilsBean.copyProperties(recipeModel, recipeAddRequest);
            recipeRepository.save(recipeModel);
            return modelMapper.map(recipeModel, RecipeResponse.class);
        }
        RecipeModel recipeModel = new RecipeModel();
        nullAwareBeanUtilsBean.copyProperties(recipeModel, recipeAddRequest);
        recipeModel.setAccesss(accesss);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy EEE");
        String formatDate = dateFormat.format(new Date());
        recipeModel.setDate(formatDate);
        recipeRepository.save(recipeModel);
        RecipeResponse recipeResponse = new RecipeResponse();
        nullAwareBeanUtilsBean.copyProperties(recipeResponse, recipeModel);
        return recipeResponse;
    }

    @Override
    public List<RecipeResponse> getAllRecipe() throws InvocationTargetException, IllegalAccessException {
        List<RecipeModel> recipeModels = recipeRepository.findAllBySoftDeleteFalse();
        List<RecipeResponse> recipeResponses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(recipeModels)) {
            for (RecipeModel recipeModel : recipeModels) {
                RecipeResponse recipeResponse = new RecipeResponse();
                nullAwareBeanUtilsBean.copyProperties(recipeResponse, recipeModel);
                recipeResponses.add(recipeResponse);
            }
        }
        return recipeResponses;
    }

    @Override
    public RecipeResponse getRecipe(String id) throws InvocationTargetException, IllegalAccessException {
        RecipeModel recipeModel = getRecipeModel(id);
        RecipeResponse recipeResponse = new RecipeResponse();
        nullAwareBeanUtilsBean.copyProperties(recipeResponse, recipeModel);
        return recipeResponse;
    }

    @Override
    public void deleteRecipe(String id) {
        RecipeModel recipeModel = getRecipeModel(id);
        recipeModel.setSoftDelete(true);
        recipeRepository.save(recipeModel);
    }

    public void updateRecipeDetail(String id, RecipeAddRequest recipeAddRequest) {
        RecipeModel recipeModel =getRecipeModel(id);
        if (recipeAddRequest != null){
            if (recipeAddRequest.getItemDescription()!=null){
                recipeModel.setItemDescription(recipeAddRequest.getItemDescription());
            }
            if (recipeAddRequest.getRecipeIngredient()!=null){
                recipeModel.setRecipeIngredient(recipeAddRequest.getRecipeIngredient());
            }
            if (recipeAddRequest.getItemName()!=null){
                recipeModel.setItemName(recipeAddRequest.getItemName());
            }
            if (recipeAddRequest.getItemUrl()!=null){
                recipeModel.setItemUrl(recipeAddRequest.getItemUrl());
            }
            recipeRepository.save(recipeModel);
        }

    }

    public void difference(RecipeModel recipeModel, RecipeAddRequest recipeAddRequest, HashMap<String, String> changedProperties) throws IllegalAccessException, InvocationTargetException {
        RecipeModel recipeModel1 = new RecipeModel();
        nullAwareBeanUtilsBean.copyProperties(recipeModel1, recipeAddRequest);
        recipeModel1.setId(recipeModel.getId());
        for (Field field : recipeModel.getClass().getDeclaredFields()) {
            // You might want to set modifier to public first (if it is not public yet)
            field.setAccessible(true);
            Object value1 = field.get(recipeModel);
            Object value2 = field.get(recipeModel1);
            if (value1 != null && value2 != null) {
                if (!Objects.equals(value1, value2)) {
                    changedProperties.put(field.getName(), value2.toString());
                }
            }
        }
    }

    @Override
    public ShoppingListLog shoppingList(String id, String loginID) {
        RecipeModel recipeModel = getRecipeModel(id);
        Login login = loginService.getLoginModel(loginID);
        ShoppingListLog shoppingListLog = new ShoppingListLog();
        loginRepository.save(login);
        shoppingListLog.setIngredients(recipeModel.getRecipeIngredient());
        shoppingListLog.setLoginId(loginID);
        shoppingListLog.setRecipeId(id);
        shoppingListLog.setItemName(recipeModel.getItemName());
        shoppingListLog.setDate(new Date());
        shoppingListLog.setSoftDelete(false);
        return shoppingListLogRepository.save(shoppingListLog);
    }

    @Override
    public Page<RecipeModel> getAllRecipeByFilterAndSortAndPage(RecipeFilter recipeFilter, FilterSortRequest.SortRequest<RecipeSortBy> sort, PageRequest pagination) {
        return recipeRepository.getAllRecipesByFilterAndSort(recipeFilter,sort,pagination);
    }

    @Override
    public List<ShoppingListLog> getShoppingList(String loginId) {
        return shoppingListLogRepository.findByLoginIdAndSoftDeleteIsFalse(loginId);
    }

    @Override
    public List<ShoppingListLog> getRecipeList(String loginId) {
        return getShoppingLog(loginId);
    }

    private RecipeModel getRecipeModel(String id) {
        return recipeRepository.findByIdAndSoftDeleteIsFalse(id).orElseThrow(() -> new NotFoundException(MessageConstant.RECIPE_ID_NOT_FOUND));
    }

    private List<ShoppingListLog> getShoppingLog(String loginId){
        return shoppingListLogRepository.findByLoginIdAndSoftDeleteIsFalse(loginId);
    }

}
