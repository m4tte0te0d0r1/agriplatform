package it.unicam.cs.ids.agriplatform.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;
    private final LocalDateTime timestamp;
    private final int status;
    private final Map<String, Object> errors; // Additional error details (e.g., field-specific validation errors)

    private ApiResponse(boolean success, String message, T data, HttpStatus status, Map<String, Object> errors) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.errors = errors;
    }

    // Success Responses
    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Success", data, HttpStatus.OK, null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return ResponseEntity.ok(new ApiResponse<>(true, message, data, HttpStatus.OK, null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Resource created successfully", data, HttpStatus.CREATED, null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, message, data, HttpStatus.CREATED, null));
    }

    public static ResponseEntity<ApiResponse<Void>> noContent() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<>(true, "No content", null, HttpStatus.NO_CONTENT, null));
    }

    // Client Error Responses
    public static <T> ResponseEntity<ApiResponse<T>> badRequest(String message) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, message, null, HttpStatus.BAD_REQUEST, null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> badRequest(String message, Map<String, Object> errors) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, message, null, HttpStatus.BAD_REQUEST, errors));
    }

    public static <T> ResponseEntity<ApiResponse<T>> unauthorized(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(false, message, null, HttpStatus.UNAUTHORIZED, null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> forbidden(String message) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse<>(false, message, null, HttpStatus.FORBIDDEN, null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> notFound(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, message, null, HttpStatus.NOT_FOUND, null));
    }

    // Server Error Responses
    public static <T> ResponseEntity<ApiResponse<T>> internalServerError(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, message, null, HttpStatus.INTERNAL_SERVER_ERROR, null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> internalServerError(String message, Map<String, Object> errors) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, message, null, HttpStatus.INTERNAL_SERVER_ERROR, errors));
    }

    // Custom Status Responses
    public static <T> ResponseEntity<ApiResponse<T>> custom(HttpStatus status, boolean success, String message, T data, Map<String, Object> errors) {
        return ResponseEntity.status(status)
                .body(new ApiResponse<>(success, message, data, status, errors));
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public Map<String, Object> getErrors() {
        return errors;
    }
}
