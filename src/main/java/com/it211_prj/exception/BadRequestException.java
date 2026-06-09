package com.it211_prj.exception;

// Exception dung cho du lieu request khong hop le ve nghiep vu.
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
