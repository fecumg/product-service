package com.tasc.productservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Truong Duc Duong
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiDataResponse {
    private List<String> messages;
    private Object data;

    public ApiDataResponse(List<String> messages) {
        this.messages = messages;
    }
}
