package com.itechart.test.altir.receiver.controller;

import com.google.gson.Gson;
import com.itechart.test.altir.receiver.controller.model.SqlAndObjsDto;
import com.itechart.test.altir.receiver.controller.model.SqlObjArgsAndTypesDto;
import com.itechart.test.altir.receiver.controller.model.SqlReqTypeArgsDto;
import com.itechart.test.altir.receiver.service.mapper.JobExecutionRowMapper;
import com.itechart.test.altir.receiver.service.mapper.RowCallbackHandlerJobExec;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobExecutionDaoController {
	private static final String URL = "/job-exec-jdbc-operation";

	private final JdbcTemplate jdbcTemplate;
	private final ApplicationContext applicationContext;
	private final Gson gson;

	public JobExecutionDaoController(JdbcTemplate jdbcTemplate,
	                                 ApplicationContext applicationContext,
	                                 Gson gson) {
		this.jdbcTemplate = jdbcTemplate;
		this.applicationContext = applicationContext;
		this.gson = gson;
	}

	@PostMapping(URL + "/query")
	public ResponseEntity<?> query(@RequestBody SqlAndObjsDto sqlAndObjsDto) {
		return ResponseEntity.ok(gson.toJson(jdbcTemplate.query(
				sqlAndObjsDto.getSql(),
				applicationContext.getBean(JobExecutionRowMapper.class),
				sqlAndObjsDto.getArgs()
		)));
	}

	@PutMapping(URL + "/update")
	public ResponseEntity<?> update(@RequestBody SqlObjArgsAndTypesDto sqlObjArgsAndTypesDto) {
		return ResponseEntity.ok(jdbcTemplate.update(
				sqlObjArgsAndTypesDto.getSql(),
				sqlObjArgsAndTypesDto.getObjects(),
				sqlObjArgsAndTypesDto.getTypes()
		));
	}

	@PostMapping(URL + "/query/object")
	public ResponseEntity<?> queryForObject(@RequestBody SqlReqTypeArgsDto sqlReqTypeArgsDto) {
		return ResponseEntity.ok(gson.toJson(jdbcTemplate.queryForObject(
				sqlReqTypeArgsDto.getSql(),
				sqlReqTypeArgsDto.getReqType(),
				sqlReqTypeArgsDto.getArgs()
		)));
	}

	@PostMapping(URL + "/object/query")
	public ResponseEntity<?> queryForObject(@RequestBody SqlAndObjsDto sqlAndObjsDto) {
		return ResponseEntity.ok(gson.toJson(jdbcTemplate.queryForObject(
				sqlAndObjsDto.getSql(),
				applicationContext.getBean(JobExecutionRowMapper.class),
				sqlAndObjsDto.getArgs()
		)));
	}

	@PostMapping(URL + "/query/rch")
	public ResponseEntity<?> queryRch(@RequestBody SqlAndObjsDto sqlAndObjsDto) {
		jdbcTemplate.query(
				sqlAndObjsDto.getSql(),
				sqlAndObjsDto.getArgs(),
				applicationContext.getBean(RowCallbackHandlerJobExec.class)
		);
		return ResponseEntity.ok("OK");
	}
}
