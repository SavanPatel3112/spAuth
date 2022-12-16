package com.example.authmoduls.service;

import com.example.authmoduls.ar.auth.decorator.RecipeAddRequest;
import com.example.authmoduls.ar.auth.decorator.RecipeResponse;
import com.example.authmoduls.ar.auth.model.RecipeModel;
import com.example.authmoduls.ar.auth.repository.LoginRepository;
import com.example.authmoduls.ar.auth.repository.RecipeRepository;
import com.example.authmoduls.ar.auth.repository.ShoppingListLogRepository;
import com.example.authmoduls.ar.auth.service.LoginService;
import com.example.authmoduls.ar.auth.service.RecipeService;
import com.example.authmoduls.ar.auth.service.RecipeServiceImpl;
import com.example.authmoduls.auth.model.Accesss;
import com.example.authmoduls.common.decorator.NotificationParser;
import com.example.authmoduls.common.decorator.NullAwareBeanUtilsBean;
import com.example.authmoduls.common.service.AdminConfigurationService;
import com.example.authmoduls.common.utils.JwtTokenUtil;
import com.example.authmoduls.common.utils.PasswordUtils;
import com.example.authmoduls.common.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@Slf4j
public class RecipeServiceImplTest {

    private static final String itemName = "string";
    private static final String itemDescription = "some mast dish";
    private static final String id = "id";
    private static final String access = String.valueOf(Accesss.ADMIN);
    private final NullAwareBeanUtilsBean nullAwareBeanUtilsBean = mock(NullAwareBeanUtilsBean.class);
    private final JwtTokenUtil jwtTokenUtil = mock(JwtTokenUtil.class);
    private final PasswordUtils passwordUtils = spy(PasswordUtils.class);
    private final AdminConfigurationService adminService = mock(AdminConfigurationService.class);
    private final Utils utils = spy(Utils.class);
    private final LoginService loginService = mock(LoginService.class);
    private final LoginRepository loginRepository = mock(LoginRepository.class);
    private final RecipeRepository recipeRepository = mock(RecipeRepository.class);
    private final ShoppingListLogRepository shoppingListLogRepository = mock(ShoppingListLogRepository.class);
    private final ModelMapper modelMapper = getModelMapper();

    private final RecipeService recipeService = new RecipeServiceImpl(nullAwareBeanUtilsBean,modelMapper,recipeRepository,loginService,loginRepository,shoppingListLogRepository);
    private ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    @Test
    void testAddOrUpdateRecipe() throws InvocationTargetException, IllegalAccessException {
        //given
        var recipeAddRequest = RecipeAddRequest.builder().itemName(itemName).itemDescription(itemDescription).build();
        var recipeModel = RecipeModel.builder().id(id).itemDescription(itemDescription).itemName(itemName).build();
        var recipeResponse = RecipeResponse.builder().id(id).itemDescription(itemDescription).build();
        when( recipeRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(recipeModel));
        doNothing().when(nullAwareBeanUtilsBean).copyProperties(recipeModel, recipeAddRequest);
        doNothing().when(nullAwareBeanUtilsBean).copyProperties(recipeResponse, recipeModel);

        //when
        recipeService.addOrUpdateRecipe(recipeAddRequest, id , Accesss.ADMIN);

        //then
        Assert.assertEquals(recipeResponse, recipeService.addOrUpdateRecipe(recipeAddRequest, id , Accesss.ADMIN));


    }

    //RecipeResponse addOrUpdateRecipe(RecipeAddRequest recipeAddRequest, String id , Accesss accesss)

}
