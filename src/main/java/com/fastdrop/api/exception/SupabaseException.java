package com.fastdrop.api.exception;

public class SupabaseException extends RuntimeException {
    public SupabaseException(String message) {
        super(message);
    }
}
