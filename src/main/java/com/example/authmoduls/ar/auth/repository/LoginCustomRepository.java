package com.example.authmoduls.ar.auth.repository;

import com.example.authmoduls.ar.auth.decorator.LoginFilter;
import com.example.authmoduls.ar.auth.decorator.LoginSortBy;
import com.example.authmoduls.ar.auth.model.Login;
import com.example.authmoduls.common.decorator.FilterSortRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface LoginCustomRepository {
    Page<Login> findAllUserByFilterAndSortAndPage(LoginFilter loginFilter, FilterSortRequest.SortRequest<LoginSortBy> sort, PageRequest pagination);
}
