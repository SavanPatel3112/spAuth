package com.example.authmoduls.cc.service;

import com.example.authmoduls.auth.decorator.UserFilter;
import com.example.authmoduls.auth.enums.UserSortBy;
import com.example.authmoduls.cc.decorator.MemberAddRequest;
import com.example.authmoduls.cc.decorator.MemberResponse;
import com.example.authmoduls.cc.enums.Plan;
import com.example.authmoduls.cc.modal.MemberModel;
import com.example.authmoduls.cc.repository.MemberRepository;
import com.example.authmoduls.common.constant.MessageConstant;
import com.example.authmoduls.common.decorator.FilterSortRequest;
import com.example.authmoduls.common.decorator.NullAwareBeanUtilsBean;
import com.example.authmoduls.common.exception.AlreadyExistException;
import com.example.authmoduls.common.exception.InvaildRequestException;
import com.example.authmoduls.common.exception.NotFoundException;
import com.example.authmoduls.common.model.AdminConfiguration;
import com.example.authmoduls.common.service.AdminConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    NullAwareBeanUtilsBean nullAwareBeanUtilsBean;
    @Autowired
    AdminConfigurationService adminService;

    @Override
    public MemberResponse addOrUpdateMember(MemberAddRequest memberAddRequest, String id, Plan plan) throws InvocationTargetException, IllegalAccessException {
        if (id != null) {
            MemberModel memberModel = getMemberModel(id);
            nullAwareBeanUtilsBean.copyProperties(memberModel, memberAddRequest);
            memberRepository.save(memberModel);
            MemberResponse memberResponse = modelMapper.map(memberModel, MemberResponse.class);
            log.info("memberResponse:{}",memberResponse);
            return memberResponse;
        } else {
            if (plan == null)//check user plan(role)
                throw new InvaildRequestException(MessageConstant.ROLE_NOT_FOUND);
        }
        checkUserDetails(memberAddRequest);//chcek empty or not
        MemberModel memberModel = new MemberModel();
        memberModel.setPlan(plan);
        memberModel.setDate(new Date());
        nullAwareBeanUtilsBean.copyProperties(memberModel, memberAddRequest);
        memberRepository.save(memberModel);
        MemberResponse memberResponse = modelMapper.map(memberModel, MemberResponse.class);
        return memberResponse;
    }

    public void checkUserDetails(MemberAddRequest memberAddRequest) throws InvocationTargetException, IllegalAccessException {
        AdminConfiguration adminConfiguration = adminService.getConfigurationDetails();
        if ((StringUtils.isEmpty(memberAddRequest.getFirstName()) || (memberAddRequest.getFirstName().matches(adminConfiguration.getNameRegex())))) {
            throw new InvaildRequestException(MessageConstant.FIRSTNAME_NOT_EMPTY);
        }
        if ((StringUtils.isEmpty(memberAddRequest.getMiddleName()) || (memberAddRequest.getMiddleName().matches(adminConfiguration.getNameRegex())))) {
            throw new InvaildRequestException(MessageConstant.MIDDLENAME_NOT_EMPTY);
        }
        if ((StringUtils.isEmpty(memberAddRequest.getLastName()) || (memberAddRequest.getLastName().matches(adminConfiguration.getNameRegex())))) {
            throw new InvaildRequestException(MessageConstant.LASTNAME_NOT_EMPTY);
        }
        if (StringUtils.isEmpty(memberAddRequest.getEmail())) {
            throw new InvaildRequestException(MessageConstant.EMAIL_NOT_FOUND);
        }
        if (memberRepository.existsByEmailAndSoftDeleteFalse(memberAddRequest.getEmail())) {
            throw new AlreadyExistException(MessageConstant.EMAIL_NAME_EXISTS);
        }
        if (StringUtils.isEmpty(memberAddRequest.getEmail()) && CollectionUtils.isEmpty(adminConfiguration.getRequiredEmailItems())) {
            throw new InvaildRequestException(MessageConstant.EMAIL_EMPTY);
        }
        if (!memberAddRequest.getEmail().matches(adminConfiguration.getRegex())) {
            throw new InvaildRequestException(MessageConstant.EMAIL_FORMAT_NOT_VALID);
        }
        if (!memberAddRequest.getPhoneNumber().matches(adminConfiguration.getMobileNoRegex())) {
            throw new InvaildRequestException(MessageConstant.INVAILD_MOBILENO);
        }
        if (StringUtils.length(memberAddRequest.getAddress().getZipCode()) > 7) {
            throw new InvaildRequestException(MessageConstant.INVAILD_ZIPCODE);
        }
    }

    @Override
    public List<MemberResponse> getAllMember() throws InvocationTargetException, IllegalAccessException {
        List<MemberModel> memberModels = memberRepository.findAllBySoftDeleteFalse();
        List<MemberResponse> memberResponses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(memberModels)) {
            for (MemberModel memberModel : memberModels) {
                MemberResponse memberResponse = new MemberResponse();
                nullAwareBeanUtilsBean.copyProperties(memberResponse, memberModel);
                memberResponses.add(memberResponse);
            }
        }
        return memberResponses;
    }

    @Override
    public MemberResponse getMember(String id) throws InvocationTargetException, IllegalAccessException {
        MemberModel memberModel = getMemberModel(id);
        MemberResponse memberResponse = new MemberResponse();
        nullAwareBeanUtilsBean.copyProperties(memberResponse, memberModel);
        //date
        //consumer
        return memberResponse;
    }

    @Override
    public List<MemberResponse> getMemberByPagination(UserFilter filter, FilterSortRequest.SortRequest<UserSortBy> sort, PageRequest pageRequest) throws InvocationTargetException, IllegalAccessException {
        return memberRepository.findAllUserByFilterAndSortAndPage(filter, sort, pageRequest);
    }

    @Override
    public void deleteMember(String id) {
        MemberModel memberModel = getMemberModel(id);
        memberModel.setSoftDelete(true);
        memberRepository.save(memberModel);
    }

    private MemberModel getMemberModel(String id) {
        return memberRepository.findByIdAndSoftDeleteIsFalse(id).orElseThrow(() -> new NotFoundException(MessageConstant.USER_ID_NOT_FOUND));
    }
}
