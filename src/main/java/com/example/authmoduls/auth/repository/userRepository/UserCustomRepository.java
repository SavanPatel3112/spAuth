package com.example.authmoduls.auth.repository.userRepository;

import com.example.authmoduls.auth.decorator.*;
import com.example.authmoduls.auth.decorator.bookDecorator.UserBookDetails;
import com.example.authmoduls.auth.enums.UserSortBy;
import com.example.authmoduls.auth.model.UserModel;
import com.example.authmoduls.common.decorator.FilterSortRequest;
import com.example.authmoduls.common.decorator.UserDateDetails;
import org.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface UserCustomRepository {
    Page<UserModel> findAllUserByFilterAndSortAndPage(UserFilter userFilter, FilterSortRequest.SortRequest<UserSortBy> sort, PageRequest pagination);

    List<UserResponse> getUser(UserFilter userFilter);

    List<UserDetailResponse> getUserResult(UserDetail userDetail);

    List<UserResultResponse> getUserResultBySemester(UserResult userResult);

    List<UserMinMaxMarkSemResponse> getUserResultByMinMaxMark(UserIdsRequest userIdsRequest);

    List<UserResultByDateRespose> getUserResultByDate(UserResultByDate userResultByDate);

    List<UserResultByStatus> getUserResultByStatus(UserIdsRequest userIdsRequest);

    Page<UserResultByStatus> findUserResultStatusByFilterAndSortAndPage(UserIdsRequest userIdsRequest, FilterSortRequest.SortRequest<UserSortBy> sort, PageRequest pagination) throws InvocationTargetException, IllegalAccessException;

    List<UserDateDetails> userChartApi(int year);

    Page<UserBookDetails> findUserBookDetailsByFilterAndSortAndPage(UserIdsRequest userIdsRequest, FilterSortRequest.SortRequest<UserDataSortBy> sort, PageRequest pagination) throws InvocationTargetException, IllegalAccessException, JSONException;

}


