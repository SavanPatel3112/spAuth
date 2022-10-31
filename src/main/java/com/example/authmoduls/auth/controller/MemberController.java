package com.example.authmoduls.auth.controller;

import com.example.authmoduls.auth.decorator.UserFilter;
import com.example.authmoduls.auth.enums.UserSortBy;
import com.example.authmoduls.cc.decorator.MemberAddRequest;
import com.example.authmoduls.cc.decorator.MemberResponse;
import com.example.authmoduls.cc.enums.Plan;
import com.example.authmoduls.cc.service.MemberService;
import com.example.authmoduls.common.constant.ResponseConstant;
import com.example.authmoduls.common.decorator.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    MemberService memberService;
    @SneakyThrows
    @RequestMapping(name = "addOrUpdateMember", value = "/addOrUpdate", method = RequestMethod.POST)
    public DataResponse<MemberResponse> addOrUpdateMember(@RequestBody MemberAddRequest memberAddRequest, @RequestParam(required = false) String id, @RequestParam(required = false) Plan plan) {
        DataResponse<MemberResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(memberService.addOrUpdateMember(memberAddRequest, id, plan));
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }
    @SneakyThrows
    @RequestMapping(name = "getAllMember", value = "/getAll", method = RequestMethod.GET)
    public ListResponse<MemberResponse> getAllMember() {
        ListResponse<MemberResponse> listResponse = new ListResponse<>();
        listResponse.setData(memberService.getAllMember());
        listResponse.setStatus(Response.getSuccessResponse(ResponseConstant.SUCCESS));
        return listResponse;
    }
    @SneakyThrows
    @RequestMapping(name = "getMember", value = "/get{id}", method = RequestMethod.GET)
    public DataResponse<MemberResponse> getMember(@RequestParam String id) {
        DataResponse<MemberResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(memberService.getMember(id));
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }
    @SneakyThrows
    @RequestMapping(name = "getMemberByPagination", value = "/getAll/filter", method = RequestMethod.POST)
    public ListResponse<MemberResponse> getMemberByPagination(@RequestBody FilterSortRequest<UserFilter, UserSortBy> filterSortRequest) {
        ListResponse<MemberResponse> listResponse = new ListResponse<>();
        UserFilter filter = filterSortRequest.getFilter();
        FilterSortRequest.SortRequest<UserSortBy> sort = filterSortRequest.getSort();
        Pagination pagination = filterSortRequest.getPage();
        PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getLimit());
        List<MemberResponse> memberResponses = memberService.getMemberByPagination(filter, sort, pageRequest);
        listResponse.setData(memberResponses);
        listResponse.setStatus(Response.getOhkResponse());
        return listResponse;
    }
    @SneakyThrows
    @RequestMapping(name = "deleteMember", value = "/delete{id}", method = RequestMethod.GET)
    public DataResponse<Object> deleteMember(@RequestParam String id) {
        DataResponse<Object> dataResponse = new DataResponse<>();
        memberService.deleteMember(id);
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }
}
