package com.mfathullah.be_product_otomotif.exception;

import java.time.LocalDateTime;

public record ApiError(int status, String message, LocalDateTime timestamp) {
    public ApiError(int status, String message) {
        this(status, message, LocalDateTime.now());
    }
}