package com.example.authmoduls.ar.auth.service;

import com.example.authmoduls.ar.auth.constant.MessageConstant;
import com.example.authmoduls.ar.auth.decorator.RecipeAddRequest;
import com.example.authmoduls.ar.auth.decorator.RecipeResponse;
import com.example.authmoduls.ar.auth.model.RecipeModel;
import com.example.authmoduls.ar.auth.repository.RecipeRepository;
import com.example.authmoduls.common.decorator.NullAwareBeanUtilsBean;
import com.example.authmoduls.common.enums.Role;
import com.example.authmoduls.common.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {
    private final NullAwareBeanUtilsBean nullAwareBeanUtilsBean;
    private final ModelMapper modelMapper;
    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(NullAwareBeanUtilsBean nullAwareBeanUtilsBean, ModelMapper modelMapper, RecipeRepository recipeRepository) {
        this.nullAwareBeanUtilsBean = nullAwareBeanUtilsBean;
        this.modelMapper = modelMapper;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public RecipeResponse addOrUpdateRecipe(RecipeAddRequest recipeAddRequest, String id, Role role) throws InvocationTargetException, IllegalAccessException {
        if (id != null) {
            RecipeModel recipeModel = getRecipeModel(id);
            nullAwareBeanUtilsBean.copyProperties(recipeModel, recipeAddRequest);
            recipeRepository.save(recipeModel);
            return modelMapper.map(recipeModel, RecipeResponse.class);
        }
        RecipeModel recipeModel = new RecipeModel();
        nullAwareBeanUtilsBean.copyProperties(recipeModel, recipeAddRequest);
        recipeModel.setRole(role);
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

    @Override
    public void recipeUpdate(String id, Role role, RecipeAddRequest recipeAddRequest) throws InvocationTargetException, IllegalAccessException {
        RecipeModel recipeModel = getRecipeModel(id);
        HashMap<String, String> changedProperties = new HashMap<>();
        boolean recipeUpdate = false;
        if (recipeUpdate){
            updateRecipeDetail(id,recipeAddRequest);
            difference(recipeModel,recipeAddRequest,changedProperties);
        }

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

    private RecipeModel getRecipeModel(String id) {
        return recipeRepository.findByIdAndSoftDeleteIsFalse(id).orElseThrow(() -> new NotFoundException(MessageConstant.RECIPE_ID_NOT_FOUND));
    }
}
