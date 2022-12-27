package com.example.authmoduls.service;

import com.example.authmoduls.ar.auth.repository.LoginRepository;
import com.example.authmoduls.ar.auth.repository.RecipeRepository;
import com.example.authmoduls.ar.auth.repository.ShoppingListLogRepository;
import com.example.authmoduls.ar.auth.service.LoginService;
import com.example.authmoduls.ar.auth.service.RecipeService;
import com.example.authmoduls.ar.auth.service.RecipeServiceImpl;
import com.example.authmoduls.auth.model.Accesss;
import com.example.authmoduls.common.decorator.NullAwareBeanUtilsBean;
import com.example.authmoduls.common.service.AdminConfigurationService;
import com.example.authmoduls.common.utils.JwtTokenUtil;
import com.example.authmoduls.common.utils.PasswordUtils;
import com.example.authmoduls.common.utils.Utils;
import com.example.authmoduls.helper.RecipeServiceTestDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
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

    private static final String id = "639b03d1665fa56d3ec3ca95";
    private static final String loginId = "6398478a1311382b51c49584";
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

/*    @Test
      void testAddOrUpdateRecipe() throws InvocationTargetException, IllegalAccessException {
        //given
        //RecipeResponse addOrUpdateRecipe(RecipeAddRequest recipeAddRequest, String id , Accesss accesss)
        var recipeModel = RecipeServiceTestDataGenerator.recipeModel();
        var recipeAddRequest = RecipeServiceTestDataGenerator.recipeAddRequest();
        when(recipeRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(recipeModel));
        var recipeResponse = RecipeServiceTestDataGenerator.recipeResponse();

        //when
        recipeService.addOrUpdateRecipe(recipeAddRequest, id , Accesss.USER, );

        //then
        Assertions.assertEquals(recipeResponse,recipeService.addOrUpdateRecipe(recipeAddRequest,id,Accesss.USER, ));

    }*/

    @Test
    void testGetAllRecipe() throws InvocationTargetException, IllegalAccessException {
        //given
        var recipeModel = RecipeServiceTestDataGenerator.recipeModelList();
        var recipeResponse = RecipeServiceTestDataGenerator.recipeResponseList();
        when(recipeRepository.findAllBySoftDeleteFalse()).thenReturn(recipeModel);

        //when
        recipeService.getAllRecipe();

        //then
        Assertions.assertEquals(recipeResponse,recipeService.getAllRecipe());

    }

    @Test
    void testGetRecipe() throws InvocationTargetException, IllegalAccessException {
        //given
        var recipeModel = RecipeServiceTestDataGenerator.recipeModel();
        var recipeResponse = RecipeServiceTestDataGenerator.recipeResponse();
        when(recipeRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(recipeModel));

        //when
        recipeService.getRecipe(id);

        //then
        Assertions.assertEquals(recipeResponse,recipeService.getRecipe(id));
    }

    @Test
    void testDeleteRecipe(){
        //given
        var recipeModel = RecipeServiceTestDataGenerator.recipeModel();
        when(recipeRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(recipeModel));
        //when
        recipeService.deleteRecipe(id);

        //then
        assert recipeModel != null;
        verify(recipeRepository).save(recipeModel);

    }

    @Test
    void testShoppingList(){
        //given
        var recipeModel = RecipeServiceTestDataGenerator.recipeModel();
        var login = RecipeServiceTestDataGenerator.login();
        var shoppingListLog = RecipeServiceTestDataGenerator.shoppingListLog();
        when(recipeRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(recipeModel));
        when(loginRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(login));

        //when
        recipeService.shoppingList(id, loginId);

        //then
        Assertions.assertEquals(shoppingListLogRepository.save(shoppingListLog),recipeService.shoppingList(id, loginId));

    }

    @Test
    void testGeneratePdfFile(){



    }

}
