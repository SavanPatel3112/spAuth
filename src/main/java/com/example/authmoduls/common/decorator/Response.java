package com.example.authmoduls.common.decorator;

import com.example.authmoduls.common.constant.ResponseConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Response {
    HttpStatus httpStatus;
    String status;
    String description;

    public static Response getOkResponse( ) {
        return new Response(HttpStatus.OK, ResponseConstant.SUCCESS, ResponseConstant.OK);
    }

    public static Response getOkSuccessResponse(String message){
        return new Response(HttpStatus.OK , ResponseConstant.SUCCESS , message);
    }

    public static Response getSuccessResponse(String success) {
        return new Response(HttpStatus.OK, ResponseConstant.SUCCESS, success);
    }

    public static Response getNotFoundResponse(String msg) {
        return new Response(HttpStatus.BAD_REQUEST, ResponseConstant.ERROR, msg);
    }

    public static Response getEmptyResponse(String msg) {
        return new Response(HttpStatus.BAD_REQUEST, ResponseConstant.ERROR, msg);
    }

    public static Response getInvaildResponse(String msg) {
        return new Response(HttpStatus.BAD_REQUEST, ResponseConstant.ERROR, msg);
    }


    public static Response getOhkResponse() {
        return new Response(HttpStatus.OK, ResponseConstant.OK, ResponseConstant.OK);
    }

    public static Response getInternalServerErrorResponse() {
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ResponseConstant.ERROR, null);
    }

    public static Response getAlreadyExists(String msg) {
        return new Response(HttpStatus.BAD_REQUEST, ResponseConstant.ERROR, msg);
    }

    public static Response getTokensucessResponse() {
        return new Response(HttpStatus.OK, ResponseConstant.OK, ResponseConstant.Token_Validate);
    }

    public static Response getLoginResponse() {
        return new Response(HttpStatus.OK, ResponseConstant.OK, ResponseConstant.Successfully_Authentication);
    }

    public static Response getOtpResponse() {
        return new Response(HttpStatus.OK, ResponseConstant.OK, ResponseConstant.Successfully_login);
    }

    public static Response getPasswordResponse() {
        return new Response(HttpStatus.OK, ResponseConstant.OK, ResponseConstant.Password_Matched);
    }

    public static Response getOtpResponses() {
        return new Response(HttpStatus.OK, ResponseConstant.OK, ResponseConstant.Otp_Matched);
    }

    public static Response logOutResponse() {
        return new Response(HttpStatus.OK, ResponseConstant.OK, ResponseConstant.Successfully_Logout);
    }

    public Response getInvalidRequestResponse(String error) {
        return new Response(HttpStatus.BAD_REQUEST, ResponseConstant.ERROR, error);
    }

    public Response getResponse(HttpStatus httpStatus, Object constant, Object constant1) {
        return new Response(httpStatus, constant.toString(), constant1.toString());
    }
}
