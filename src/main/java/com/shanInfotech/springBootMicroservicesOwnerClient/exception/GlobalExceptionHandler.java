package com.shanInfotech.springBootMicroservicesOwnerClient.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

 @ExceptionHandler(OwnerIdNotFoundException.class)
 public ResponseEntity<Map<String, String>> handleOwnerNotFound(OwnerIdNotFoundException ex) {
     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
         Map.of("message", ex.getMessage())
     );
 }

 // (Optional) Generic fallback
 @ExceptionHandler(Exception.class)
 public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
         Map.of("message", "An unexpected error occurred.")
     );
 }
}

