package com.itechart.test.altir.receiver.controller.advice;

import com.itechart.test.altir.receiver.service.exception.DataInputException;
import com.itechart.test.altir.receiver.service.exception.DuplicateProductException;
import com.itechart.test.altir.receiver.service.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {
	@ExceptionHandler(DuplicateProductException.class)
	public ResponseEntity<?> handleDuplicateProductException(DuplicateProductException duplicateProductException) {
		return ResponseEntity.badRequest().body(duplicateProductException.getMessage());
	}

	@ExceptionHandler(DataInputException.class)
	public ResponseEntity<?> handleDataInputException(DataInputException dataInputException) {
		return ResponseEntity.badRequest().body(dataInputException.getMessage());
	}

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<?> handleProductNotFoundException(ProductNotFoundException productNotFoundException) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(productNotFoundException.getMessage());
	}
}
