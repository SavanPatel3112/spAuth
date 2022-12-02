package com.example.authmoduls.cc.repository;

import com.example.authmoduls.auth.decorator.UserFilter;
import com.example.authmoduls.auth.enums.UserSortBy;
import com.example.authmoduls.cc.decorator.MemberResponse;
import com.example.authmoduls.common.decorator.FilterSortRequest;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface MemberCustomRepository {

    List<MemberResponse> findAllUserByFilterAndSortAndPage(UserFilter filter, FilterSortRequest.SortRequest<UserSortBy> sort, PageRequest pagination) throws InvocationTargetException, IllegalAccessException;

}
