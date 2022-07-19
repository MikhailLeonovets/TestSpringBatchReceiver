package com.itechart.test.altir.receiver.controller;

import com.itechart.test.altir.receiver.controller.model.JdbcOperationDto;
import com.itechart.test.altir.receiver.controller.model.JdbcOperationResultDto;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RestController
public class RemoteJdbcOperationController {
	private static final String URL = "/jdbc-operation";

	private final JdbcOperations jdbcOperations;

	@Autowired
	public RemoteJdbcOperationController(JdbcOperations jdbcOperations) {
		this.jdbcOperations = jdbcOperations;
	}

	@GetMapping(URL)
	public ResponseEntity<?> jdbcOperation(@RequestBody JdbcOperationDto jdbcOperationDto)
			throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Method jdbcMethod = jdbcOperations.getClass().getDeclaredMethod(jdbcOperationDto.getMethodName(),
				(Class<?>[]) jdbcOperationDto.getArguments()
						.stream()
						.map(ImmutablePair::getLeft)
						.toArray());
		Object resultValue = jdbcMethod.invoke(jdbcOperations, jdbcOperationDto.getArguments()
				.stream()
				.map(ImmutablePair::getRight)
				.toArray());
		return ResponseEntity.ok(new JdbcOperationResultDto(jdbcMethod.getReturnType(), resultValue));
	}
}
