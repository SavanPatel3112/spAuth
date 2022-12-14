package com.example.authmoduls.common.advice;

import com.example.authmoduls.common.decorator.DataResponse;
import com.example.authmoduls.common.decorator.Response;
import com.example.authmoduls.common.exception.AlreadyExistException;
import com.example.authmoduls.common.exception.EmptyException;
import com.example.authmoduls.common.exception.InvaildRequestException;
import com.example.authmoduls.common.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GeneralExceptionHandler {
    

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<DataResponse<Object>> getError(HttpServletRequest req, NotFoundException ex) {
        return new ResponseEntity<>(new DataResponse<>(null, Response.getNotFoundResponse(ex.getMessage())), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DataResponse<Object>> getError(HttpServletRequest req, Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(new DataResponse<>(null, Response.getInternalServerErrorResponse()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<DataResponse<Object>> getError(HttpServletRequest req, AlreadyExistException ex) {
        return new ResponseEntity<>(new DataResponse<>(null, Response.getAlreadyExists(ex.getMessage())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyException.class)
    public ResponseEntity<DataResponse<Object>> getError(HttpServletRequest req, EmptyException ex) {
        return new ResponseEntity<>(new DataResponse<>(null, Response.getEmptyResponse(ex.getMessage())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvaildRequestException.class)
    public ResponseEntity<DataResponse<Object>> getError(HttpServletRequest req, InvaildRequestException ex) {
        return new ResponseEntity<>(new DataResponse<>(null, Response.getInvaildResponse(ex.getMessage())), HttpStatus.NOT_FOUND);
    }

}