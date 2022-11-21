package com.example.authmoduls.cc.service;

import com.example.authmoduls.auth.decorator.UserFilter;
import com.example.authmoduls.auth.enums.UserSortBy;
import com.example.authmoduls.cc.decorator.MemberAddRequest;
import com.example.authmoduls.cc.decorator.MemberResponse;
import com.example.authmoduls.cc.enums.Plan;
import com.example.authmoduls.common.decorator.FilterSortRequest;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface MemberService {
    MemberResponse addOrUpdateMember(MemberAddRequest memberAddRequest, String id, Plan plan) throws InvocationTargetException, IllegalAccessException;

    List<MemberResponse> getAllMember() throws InvocationTargetException, IllegalAccessException;

    MemberResponse getMember(String id) throws InvocationTargetException, IllegalAccessException;

    List<MemberResponse> getMemberByPagination(UserFilter filter, FilterSortRequest.SortRequest<UserSortBy> sort, PageRequest pageRequest) throws InvocationTargetException, IllegalAccessException;

    void deleteMember(String id);
}
