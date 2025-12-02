package com.alice.muzyka.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ConflictException::class)
    fun handleConflictException(ex: ConflictException): ResponseEntity<Map<String, Any?>> {
        val body = mutableMapOf<String, Any?>()
        body["message"] = ex.message
        if (ex.details != null) {
            body["details"] = ex.details
        }
        return ResponseEntity(body, HttpStatus.CONFLICT)
    }
}
