package com.example.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {
	@ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        // HTTP 409 Conflict 상태 코드와 함께 서비스에서 보낸 에러 메시지를 응답
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
