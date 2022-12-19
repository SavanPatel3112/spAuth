package com.example.authmoduls.auth.controller;
import com.example.authmoduls.common.decorator.AdminResponse;
import com.example.authmoduls.common.decorator.DataResponse;
import com.example.authmoduls.common.decorator.Response;
import com.example.authmoduls.common.enums.Role;
import com.example.authmoduls.common.model.AdminConfiguration;
import com.example.authmoduls.common.service.AdminConfigurationService;
import com.example.authmoduls.common.utils.Access;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/admin/configuration")
public class AdminConfigurationController {
    @Autowired
    AdminConfigurationService adminService;
    @SneakyThrows
    @Access(levels = Role.ADMIN)
    @RequestMapping(name = "addConfiguration",value = "/add",method = RequestMethod.GET)
    public DataResponse<AdminResponse> addConfiguration(){
        DataResponse<AdminResponse> dataResponse=new DataResponse<>();
        dataResponse.setData(adminService.addConfiguration());
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }
    @Access(levels = Role.ADMIN)
    @SneakyThrows
    @RequestMapping(name = "getConfigurationDetails",value = "/getDetail",method = RequestMethod.GET)
    public DataResponse<AdminConfiguration> getConfigurationDetails(){
        DataResponse<AdminConfiguration> dataResponse=new DataResponse<>();
        dataResponse.setData(adminService.getConfigurationDetails());
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }
}
