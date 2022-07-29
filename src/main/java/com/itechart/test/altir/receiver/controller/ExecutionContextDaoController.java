package com.itechart.test.altir.receiver.controller;

import com.google.gson.Gson;
import com.itechart.test.altir.receiver.controller.model.SqlAndBatchPssDto;
import com.itechart.test.altir.receiver.controller.model.SqlAndObjsDto;
import com.itechart.test.altir.receiver.controller.model.SqlAndPrepStSetterDto;
import com.itechart.test.altir.receiver.service.prep_statement.BatchPreparedStatementSetterExecCont;
import com.itechart.test.altir.receiver.service.mapper.ExecutionContextRowMapper;
import com.itechart.test.altir.receiver.service.prep_statement.PreparedStatementSetterExecCont;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExecutionContextDaoController {
	private static final String URL = "/exec-cont-jdbc-operation";

	private final JdbcTemplate jdbcTemplate;
	private final ApplicationContext applicationContext;
	private final Gson gson;

	public ExecutionContextDaoController(JdbcTemplate jdbcTemplate,
	                                     ApplicationContext applicationContext,
	                                     Gson gson) {
		this.jdbcTemplate = jdbcTemplate;
		this.applicationContext = applicationContext;
		this.gson = gson;
	}

	@PutMapping(URL + "/update/pss")
	public ResponseEntity<?> update(@RequestBody SqlAndPrepStSetterDto sqlAndPrepStSetterDto) {
		return ResponseEntity.ok(jdbcTemplate.update(sqlAndPrepStSetterDto.getSql(),
				new PreparedStatementSetterExecCont(sqlAndPrepStSetterDto.getStringSerializedContexts(),
						sqlAndPrepStSetterDto.getExecutionId())));
	}

	@PostMapping(URL + "/query")
	public ResponseEntity<?> query(@RequestBody SqlAndObjsDto sqlAndIdDto) {
		return ResponseEntity.ok(gson.toJson(jdbcTemplate.query(sqlAndIdDto.getSql(),
				applicationContext.getBean(ExecutionContextRowMapper.class),
				sqlAndIdDto.getArgs())));
	}

	@PutMapping(URL + "/batch-upd/bpss")
	public ResponseEntity<?> batchUpdate(@RequestBody SqlAndBatchPssDto sqlAndBatchPssDto) {
		return ResponseEntity.ok(jdbcTemplate.batchUpdate(sqlAndBatchPssDto.getSql(),
				new BatchPreparedStatementSetterExecCont(sqlAndBatchPssDto.getSerializedContexts())));
	}

}
