package com.example.authmoduls.ar.auth.service;

import com.example.authmoduls.ar.auth.constant.MessageConstant;
import com.example.authmoduls.ar.auth.decorator.RecipeAddRequest;
import com.example.authmoduls.ar.auth.decorator.RecipeResponse;
import com.example.authmoduls.ar.auth.model.RecipeModel;
import com.example.authmoduls.ar.auth.repository.RecipeRepository;
import com.example.authmoduls.common.decorator.NullAwareBeanUtilsBean;
import com.example.authmoduls.common.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

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
    public RecipeResponse addOrUpdateRecipe(RecipeAddRequest recipeAddRequest, String id) throws InvocationTargetException, IllegalAccessException {
        if (id != null) {
            RecipeModel recipeModel = getRecipeModel(id);
            nullAwareBeanUtilsBean.copyProperties(recipeModel, recipeAddRequest);
            recipeRepository.save(recipeModel);
            return modelMapper.map(recipeModel, RecipeResponse.class);
        }
        RecipeModel recipeModel = new RecipeModel();
        nullAwareBeanUtilsBean.copyProperties(recipeModel, recipeAddRequest);
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

    private RecipeModel getRecipeModel(String id) {
        return recipeRepository.findByIdAndSoftDeleteIsFalse(id).orElseThrow(() -> new NotFoundException(MessageConstant.RECIPE_ID_NOT_FOUND));
    }
}
