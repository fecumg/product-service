package com.tasc.productservice.controllers;

import com.tasc.productservice.models.ApiDataResponse;
import com.tasc.productservice.models.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author Truong Duc Duong
 */
public class BaseController {
    public ResponseEntity<ApiDataResponse> createResponse(ApiDataResponse apiDataResponse) {
        return new ResponseEntity<>(apiDataResponse, HttpStatus.OK);
    }

    public ResponseEntity<ApiDataResponse> createResponse(ApiDataResponse apiDataResponse, HttpStatus httpStatus) {
        return new ResponseEntity<>(apiDataResponse, httpStatus);
    }
}
