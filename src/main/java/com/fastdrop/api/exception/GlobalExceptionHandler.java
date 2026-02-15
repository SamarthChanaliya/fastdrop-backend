package com.fastdrop.api.exception;

import com.fastdrop.api.wrapper.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SupabaseException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public Mono<ApiResponse<Void>> handleSupabaseError(SupabaseException ex) {
        log.error("Supabase Error: {}", ex.getMessage());
        return Mono.just(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ApiResponse<Map<String, String>>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return Mono.just(ApiResponse.error("Validation failed", errors));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ApiResponse<Map<String, String>>> handleGenericError(Exception ex) {
        log.error("Unhandled Exception: ", ex);

        Map<String, String> errorData = new HashMap<>();
        errorData.put("details", ex.getMessage());

        return Mono.just(ApiResponse.error("Internal server error", errorData));
    }
}