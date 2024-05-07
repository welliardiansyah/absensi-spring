package com.absensi.absensi.response;

import java.util.*;
import org.springframework.http.*;

public class ResponseHandler {
    public static ResponseEntity<Object> successResponseBuilder(String message, HttpStatus httpStatus, Object responseObject) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("status", httpStatus);
        response.put("data", responseObject);

        if (httpStatus.is2xxSuccessful()) {
            response.put("code", 200);
        } else if(httpStatus.is4xxClientError()) {
            response.put("code", 400);
        } else if(httpStatus.is5xxServerError()) {
            response.put("code", 500);
        } else if (httpStatus.isError()) {
            response.put("code", 401);
        }

        return new ResponseEntity<>(response, httpStatus);
    }

    public static ResponseEntity<Object> errorResponseBuilder(String errorMessage, HttpStatus httpStatus) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", errorMessage);
        errorResponse.put("status", httpStatus);

        if (httpStatus.is2xxSuccessful()) {
            errorResponse.put("code", 200);
        } else if(httpStatus.is4xxClientError()) {
            errorResponse.put("code", 400);
        } else if(httpStatus.is5xxServerError()) {
            errorResponse.put("code", 500);
        } else if (httpStatus.isError()) {
            errorResponse.put("code", 401);
        }

        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
