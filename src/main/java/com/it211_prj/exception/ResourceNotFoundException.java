package com.it211_prj.exception;

// Exception dung khi khong tim thay entity theo id/email/token.
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
