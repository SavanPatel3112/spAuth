package com.example.authmoduls.ar.auth.service;

import com.example.authmoduls.ar.auth.constant.MessageConstant;
import com.example.authmoduls.ar.auth.decorator.ShoppingAddRequest;
import com.example.authmoduls.ar.auth.decorator.ShoppingResponse;
import com.example.authmoduls.ar.auth.model.Shopping;
import com.example.authmoduls.ar.auth.repository.ShoppingRepository;
import com.example.authmoduls.auth.model.Accesss;
import com.example.authmoduls.common.decorator.NullAwareBeanUtilsBean;
import com.example.authmoduls.common.exception.InvaildRequestException;
import com.example.authmoduls.common.exception.NotFoundException;
import com.example.authmoduls.common.model.JWTUser;
import com.example.authmoduls.common.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

@Service
@Slf4j
public class ShoppingServiceImpl implements ShoppingService{

    private final NullAwareBeanUtilsBean nullAwareBeanUtilsBean;
    private final ModelMapper modelMapper;
    private final ShoppingRepository shoppingRepository;
    private final JwtTokenUtil jwtTokenUtil;


    public ShoppingServiceImpl(NullAwareBeanUtilsBean nullAwareBeanUtilsBean, ModelMapper modelMapper, ShoppingRepository shoppingRepository,JwtTokenUtil jwtTokenUtil) {
        this.nullAwareBeanUtilsBean = nullAwareBeanUtilsBean;
        this.modelMapper = modelMapper;
        this.shoppingRepository = shoppingRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public ShoppingResponse addOrUpdateShopping(ShoppingAddRequest shoppingAddRequest, Accesss accesss, String id) throws InvocationTargetException, IllegalAccessException {
        if (id != null) {
            Shopping shopping = getShoppingModel(id);
            nullAwareBeanUtilsBean.copyProperties(shopping, shoppingAddRequest);
            shoppingRepository.save(shopping);
            return modelMapper.map(shopping,ShoppingResponse.class);
        } else {
            if (accesss == null)//check user role
                throw new InvaildRequestException(com.example.authmoduls.common.constant.MessageConstant.ROLE_NOT_FOUND);
        }
        Shopping shopping = new Shopping();
        nullAwareBeanUtilsBean.copyProperties(shopping, shoppingAddRequest);
        shopping.setAccesss(accesss);
        shoppingRepository.save(shopping);
        ShoppingResponse shoppingResponse = new ShoppingResponse();
        nullAwareBeanUtilsBean.copyProperties(shoppingResponse, shopping);
        return shoppingResponse;
    }

    @Override
    public ShoppingResponse getShoppingList(String id) throws InvocationTargetException, IllegalAccessException {

        Shopping shopping = getShoppingModel(id);
        ShoppingResponse shoppingResponse = new ShoppingResponse();
        nullAwareBeanUtilsBean.copyProperties(shoppingResponse, shopping);
        return shoppingResponse;
    }

    @Override
    public void deleteShoppingList(String id) {
        Shopping shopping = getShoppingModel(id);
        shopping.setSoftDelete(true);
        shoppingRepository.save(shopping);
    }

    @Override
    public ShoppingResponse getToken(String id) throws InvocationTargetException, IllegalAccessException {
        Shopping shopping = getShoppingModel(id);
        ShoppingResponse shoppingResponse = new ShoppingResponse();
        shoppingResponse.setAccesss(shopping.getAccesss());
        JWTUser jwtUser = new JWTUser(id, Collections.singletonList(shoppingResponse.getAccesss().toString()));
        String token = jwtTokenUtil.generateToken(jwtUser);
        nullAwareBeanUtilsBean.copyProperties(shoppingResponse, shopping);
        shoppingResponse.setToken(token);
        return shoppingResponse;

    }

    private Shopping getShoppingModel(String id) {
        return shoppingRepository.findByIdAndSoftDeleteIsFalse(id).orElseThrow(() -> new NotFoundException(MessageConstant.RECIPE_ID_NOT_FOUND));
    }

}
