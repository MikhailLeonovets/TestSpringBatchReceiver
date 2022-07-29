package com.itechart.test.altir.receiver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.itechart.test.altir.receiver.controller.model.SqlAndObjsMethodJobExecDto;
import com.itechart.test.altir.receiver.controller.model.SqlArgsReqTypesDto;
import com.itechart.test.altir.receiver.controller.model.SqlObjArgsAndTypesDto;
import com.itechart.test.altir.receiver.controller.model.SqlStepExecsDto;
import com.itechart.test.altir.receiver.service.prep_statement.BatchPreparedStatementSetterStepExec;
import com.itechart.test.altir.receiver.service.mapper.StepExecutionRowMapper;
import com.itechart.test.altir.receiver.service.mapper.StepExecutionRowMapperSpecial;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StepExecutionDaoController {
	private static final String URL = "/step-exec-jdbc-operation";

	private final JdbcOperations jdbcOperations;
	private final ApplicationContext applicationContext;
	private final Gson gson;
	private final ObjectMapper objectMapper;

	public StepExecutionDaoController(JdbcOperations jdbcOperations,
	                                  ApplicationContext applicationContext,
	                                  Gson gson,
	                                  ObjectMapper objectMapper) {
		this.jdbcOperations = jdbcOperations;
		this.applicationContext = applicationContext;
		this.gson = gson;
		this.objectMapper = objectMapper;
	}

	@PutMapping(URL + "/update")
	public ResponseEntity<?> update(@RequestBody SqlObjArgsAndTypesDto sqlObjArgsAndTypesDto) {
		return ResponseEntity.ok(jdbcOperations.update(
				sqlObjArgsAndTypesDto.getSql(),
				sqlObjArgsAndTypesDto.getObjects(),
				sqlObjArgsAndTypesDto.getTypes()
		));
	}

	@PutMapping(URL + "/batch-update")
	public ResponseEntity<?> batchUpdate(@RequestBody SqlStepExecsDto sqlStepExecsDto) {
		return ResponseEntity.ok(jdbcOperations.batchUpdate(sqlStepExecsDto.getSql(),
				new BatchPreparedStatementSetterStepExec(sqlStepExecsDto.getStepExecutions())));
	}

	@PostMapping(URL + "/query/object")
	public ResponseEntity<?> queryForObject(@RequestBody SqlArgsReqTypesDto sqlArgsReqTypesDto) {
		return ResponseEntity.ok(gson.toJson(jdbcOperations.queryForObject(
				sqlArgsReqTypesDto.getSql(),
				sqlArgsReqTypesDto.getArgs(),
				sqlArgsReqTypesDto.getReqType()
		)));
	}

	@SneakyThrows
	@PostMapping(URL + "/query")
	public ResponseEntity<?> query(@RequestBody String string) {
		SqlAndObjsMethodJobExecDto sqlAndObjsMethodJobExecDto = objectMapper.readValue(string,
				SqlAndObjsMethodJobExecDto.class);
		String gsonResponse = gson.toJson(jdbcOperations.query(
				sqlAndObjsMethodJobExecDto.getSql(),
				getRowMapperByMethod(sqlAndObjsMethodJobExecDto),
				sqlAndObjsMethodJobExecDto.getArgs()
		));
		return ResponseEntity.ok(gsonResponse);
	}

	private RowMapper getRowMapperByMethod(SqlAndObjsMethodJobExecDto sqlAndObjsMethodJobExecDto) {
		if ("getLastStepExecution".equals(sqlAndObjsMethodJobExecDto.getMethod())) {
			return applicationContext.getBean(StepExecutionRowMapperSpecial.class);
		}
		return new StepExecutionRowMapper(sqlAndObjsMethodJobExecDto.getJobExecution());
	}
}
