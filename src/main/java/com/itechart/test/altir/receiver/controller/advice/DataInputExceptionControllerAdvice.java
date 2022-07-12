package com.itechart.test.altir.receiver.controller.advice;

import com.itechart.test.altir.receiver.service.exception.DataInputException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DataInputExceptionControllerAdvice {
	@ExceptionHandler(DataInputException.class)
	public ResponseEntity<?> handleDataInputException(DataInputException dataInputException) {
		return ResponseEntity.badRequest().body(dataInputException.getMessage());
	}

}
