package com.mogun.backend;

import com.mogun.backend.service.ServiceStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Getter
public class ApiResponse<T> {

    private int code;
    private HttpStatus status;
    private String message;
    private T data;

    public ApiResponse(HttpStatus status, String message, T data) {
        this.code = status.value();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return of(OK, "SUCCESS", data);
    }

    // Seongmin 단순 accept return
    public static <T> ApiResponse<T> accept() { return of(HttpStatus.ACCEPTED, "SUCCESS", null); }

    public static <T> ApiResponse<T> badRequest(String message) {
        return of(BAD_REQUEST, message, null);
    }

    public static <T> ApiResponse<T> postAndPutResponse(ServiceStatus result, T data) {

        if(result.getStatus() != 100)
            return badRequest(result.getMessage());

        return of(HttpStatus.ACCEPTED, result.getMessage(), data);
    }
}
