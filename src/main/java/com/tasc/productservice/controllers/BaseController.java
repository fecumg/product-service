package com.tasc.productservice.controllers;

import com.tasc.productservice.models.ApiDataResponse;
import com.tasc.productservice.models.Result;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Truong Duc Duong
 */
public class BaseController {

    public ResponseEntity<ApiDataResponse> createResponse(Result result) {
        List<String> messages = new ArrayList<>();
        ApiDataResponse apiDataResponse;
        HttpStatus httpStatus;

        messages.add(result.getMessage());
        if (result.getCode() == 0) {
            httpStatus = HttpStatus.OK;
            apiDataResponse = new ApiDataResponse(messages, result.getData());
        } else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            apiDataResponse = new ApiDataResponse(messages);
        }
        return new ResponseEntity<>(apiDataResponse, httpStatus);
    }

    public ResponseEntity<ApiDataResponse> createResponse(ApiDataResponse apiDataResponse, HttpStatus httpStatus) {
        return new ResponseEntity<>(apiDataResponse, httpStatus);
    }

    public ResponseEntity<ApiDataResponse> createBindingErrorResponse(BindingResult bindingResult) {
        List<String> messages = new ArrayList<>();
        for (ObjectError error: bindingResult.getAllErrors()
        ) {
            messages.add(error.getDefaultMessage());
        }
        ApiDataResponse apiDataResponse = new ApiDataResponse(messages);
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(apiDataResponse, httpStatus);
    }
}
