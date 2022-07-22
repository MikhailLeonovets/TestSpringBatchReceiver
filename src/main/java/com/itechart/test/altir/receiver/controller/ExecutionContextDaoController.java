package com.itechart.test.altir.receiver.controller;

import com.itechart.test.altir.receiver.controller.model.SqlAndBatchPssDto;
import com.itechart.test.altir.receiver.controller.model.SqlAndIdDto;
import com.itechart.test.altir.receiver.controller.model.SqlAndPrepStSetterDto;
import com.itechart.test.altir.receiver.service.mapper.BatchPreparedStatementSetterExecCont;
import com.itechart.test.altir.receiver.service.mapper.ExecutionContextRowMapper;
import com.itechart.test.altir.receiver.service.mapper.PreparedStatementSetterExecCont;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExecutionContextDaoController {
	private static final String URL = "/exec-cont-jdbc-operation";

	private final JdbcTemplate jdbcTemplate;
	private final ApplicationContext applicationContext;

	public ExecutionContextDaoController(JdbcTemplate jdbcTemplate,
	                                     ApplicationContext applicationContext) {
		this.jdbcTemplate = jdbcTemplate;
		this.applicationContext = applicationContext;
	}

	@PutMapping(URL + "/update/pss")
	public ResponseEntity<?> update(@RequestBody SqlAndPrepStSetterDto sqlAndPrepStSetterDto) {
		return ResponseEntity.ok(jdbcTemplate.update(sqlAndPrepStSetterDto.getSql(),
				new PreparedStatementSetterExecCont(sqlAndPrepStSetterDto.getStringSerializedContexts(),
						sqlAndPrepStSetterDto.getExecutionId())));
	}

	@PostMapping(URL + "/query")
	public ResponseEntity<?> query(@RequestBody SqlAndIdDto sqlAndIdDto) {
		return ResponseEntity.ok(jdbcTemplate.query(sqlAndIdDto.getSql(),
				applicationContext.getBean(ExecutionContextRowMapper.class),
				sqlAndIdDto.getId()));
	}

	@PutMapping(URL + "/batch-upd/bpss")
	public ResponseEntity<?> batchUpdate(@RequestBody SqlAndBatchPssDto sqlAndBatchPssDto) {
		return ResponseEntity.ok(jdbcTemplate.batchUpdate(sqlAndBatchPssDto.getSql(),
				new BatchPreparedStatementSetterExecCont(sqlAndBatchPssDto.getSerializedContexts())));
	}

}
