package com.itechart.test.altir.receiver.controller;

import com.itechart.test.altir.receiver.controller.model.SqlAndObjsMethodDto;
import com.itechart.test.altir.receiver.controller.model.SqlArgsReqTypesDto;
import com.itechart.test.altir.receiver.controller.model.SqlObjArgsAndTypesDto;
import com.itechart.test.altir.receiver.controller.model.SqlStepExecsDto;
import com.itechart.test.altir.receiver.service.mapper.BatchPreparedStatementSetterStepExec;
import com.itechart.test.altir.receiver.service.mapper.StepExecutionRowMapper;
import com.itechart.test.altir.receiver.service.mapper.StepExecutionRowMapperSpecial;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StepExecutionDaoController {
	private static final String URL = "/step-exec-jdbc-operation";

	private final JdbcOperations jdbcOperations;
	private final ApplicationContext applicationContext;

	public StepExecutionDaoController(JdbcOperations jdbcOperations,
	                                  ApplicationContext applicationContext) {
		this.jdbcOperations = jdbcOperations;
		this.applicationContext = applicationContext;
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
		return ResponseEntity.ok(jdbcOperations.queryForObject(
						sqlArgsReqTypesDto.getSql(),
						sqlArgsReqTypesDto.getArgs(),
						sqlArgsReqTypesDto.getReqType()
				)
		);
	}

	@PostMapping(URL + "/query")
	public ResponseEntity<?> query(@RequestBody SqlAndObjsMethodDto sqlAndObjsMethodDto) {
		return ResponseEntity.ok(jdbcOperations.query(
				sqlAndObjsMethodDto.getSql(),
				getRowMapperByMethod(sqlAndObjsMethodDto.getMethod()),
				sqlAndObjsMethodDto.getArgs()
		));
	}

	private RowMapper getRowMapperByMethod(String method) {
		if (method.equals("getLastStepExecution")) {
			return applicationContext.getBean(StepExecutionRowMapperSpecial.class);
		}
		return applicationContext.getBean(StepExecutionRowMapper.class);
	}
}
