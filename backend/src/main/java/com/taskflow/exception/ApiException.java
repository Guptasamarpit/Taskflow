package com.taskflow.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    public final HttpStatus status;

    public ApiException(HttpStatus s, String m) {
        super(m);
        this.status = s;
    }
}
