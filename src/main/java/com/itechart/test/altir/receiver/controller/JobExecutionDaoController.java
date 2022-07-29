package com.itechart.test.altir.receiver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.itechart.test.altir.receiver.controller.model.SqlAndObjsDto;
import com.itechart.test.altir.receiver.controller.model.SqlObjArgsAndTypesDto;
import com.itechart.test.altir.receiver.controller.model.SqlObjsJobInsJsonDto;
import com.itechart.test.altir.receiver.controller.model.SqlReqTypeArgsDto;
import com.itechart.test.altir.receiver.service.mapper.JobExecutionRowMapper;
import com.itechart.test.altir.receiver.service.handler.RowCallbackHandlerJobExec;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobInstance;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class JobExecutionDaoController {
	private static final String URL = "/job-exec-jdbc-operation";

	private final JdbcTemplate jdbcTemplate;
	private final ApplicationContext applicationContext;
	private final Gson gson;
	private final ObjectMapper objectMapper;

	public JobExecutionDaoController(JdbcTemplate jdbcTemplate,
	                                 ApplicationContext applicationContext,
	                                 Gson gson,
	                                 ObjectMapper objectMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.applicationContext = applicationContext;
		this.gson = gson;
		this.objectMapper = objectMapper;
	}

	@SneakyThrows
	@PostMapping(URL + "/query")
	public ResponseEntity<?> query(@RequestBody SqlObjsJobInsJsonDto sqlObjsJobInsJsonDto) {
		JobExecutionRowMapper rowMapper = applicationContext.getBean(JobExecutionRowMapper.class);
		JobInstance jobInstance = objectMapper.readValue(sqlObjsJobInsJsonDto.getJobInstanceJson(),
				JobInstance.class);
		log.info(jobInstance.toString());
		rowMapper.setJobInstance(jobInstance);
		String str = gson.toJson(jdbcTemplate.query(
				sqlObjsJobInsJsonDto.getSql(),
				rowMapper,
				sqlObjsJobInsJsonDto.getArgs()));
		log.info(str);
		return ResponseEntity.ok(str);
	}

	@PutMapping(URL + "/update")
	public ResponseEntity<?> update(@RequestBody String sqlObjArgsAndTypesDtoJson) {
		SqlObjArgsAndTypesDto sqlObjArgsAndTypesDto = gson.fromJson(sqlObjArgsAndTypesDtoJson, SqlObjArgsAndTypesDto.class);
		return ResponseEntity.ok(jdbcTemplate.update(
				sqlObjArgsAndTypesDto.getSql(),
				sqlObjArgsAndTypesDto.getObjects(),
				sqlObjArgsAndTypesDto.getTypes()
		));
	}

	@PostMapping(URL + "/query/object")
	public ResponseEntity<?> queryForObject(@RequestBody SqlReqTypeArgsDto sqlReqTypeArgsDto) {
		log.info(sqlReqTypeArgsDto.toString());
		String gsonResponse = gson.toJson(jdbcTemplate.queryForObject(
				sqlReqTypeArgsDto.getSql(),
				sqlReqTypeArgsDto.getReqType(),
				sqlReqTypeArgsDto.getArgs()
		));
		log.info(gsonResponse);
		return ResponseEntity.ok(gsonResponse);
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
