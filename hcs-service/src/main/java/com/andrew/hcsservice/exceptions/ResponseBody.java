package com.andrew.hcsservice.exceptions;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseBody<T> {
    private int status;
    private Date timestamp;

    private T data;

    public ResponseBody(int status, T data) {
        this.status = status;
        this.data = data;
        this.timestamp = new Date();
    }
}
