package com.absensi.absensi.exception;
import org.springframework.http.*;

public class Exception {
    private final String message;
    private final Throwable throwable;
    private  final HttpStatus httpStatus;

    public Exception(String message, Throwable throwable, HttpStatus httpStatus) {
        this.message = message;
        this.throwable = throwable;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
