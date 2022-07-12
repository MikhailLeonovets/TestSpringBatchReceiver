package com.itechart.test.altir.receiver.controller.advice;

import com.itechart.test.altir.receiver.service.exception.ProductException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProductExceptionControllerAdvice {
	@ExceptionHandler(ProductException.class)
	public ResponseEntity<?> handleProductException(ProductException productException) {
		return ResponseEntity.badRequest().body(productException.getMessage());
	}
}
