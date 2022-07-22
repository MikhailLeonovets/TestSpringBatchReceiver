package com.itechart.test.altir.receiver.controller;

import com.itechart.test.altir.receiver.controller.model.SqlAndObjsDto;
import com.itechart.test.altir.receiver.controller.model.SqlAndObjsStartCountDto;
import com.itechart.test.altir.receiver.controller.model.SqlObjArgsAndTypesDto;
import com.itechart.test.altir.receiver.controller.model.SqlReqTypeArgsDto;
import com.itechart.test.altir.receiver.service.mapper.JobInstanceRowMapper;
import org.springframework.batch.core.JobInstance;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class JobInstanceDaoController {
	private static final String URL = "/job-inst-jdbc-operation";

	private final JdbcOperations jdbcOperations;
	private final ApplicationContext applicationContext;

	public JobInstanceDaoController(JdbcOperations jdbcOperations,
	                                ApplicationContext applicationContext) {
		this.jdbcOperations = jdbcOperations;
		this.applicationContext = applicationContext;
	}

	@PutMapping(URL + "/update")
	public ResponseEntity<?> update(@RequestBody SqlObjArgsAndTypesDto sqlObjArgsAndTypesDto) {
		return ResponseEntity.ok(jdbcOperations.update(sqlObjArgsAndTypesDto.getSql(),
				sqlObjArgsAndTypesDto.getObjects(),
				sqlObjArgsAndTypesDto.getTypes()));
	}

	@PostMapping(URL + "/query/list")
	public ResponseEntity<?> query(@RequestBody SqlAndObjsDto dto) {
		return ResponseEntity.ok(jdbcOperations.query(dto.getSql(),
				applicationContext.getBean(JobInstanceRowMapper.class),
				dto.getArgs()));
	}

	@PostMapping(URL + "/query/string")
	public ResponseEntity<?> query(@RequestBody String sql) {
		return ResponseEntity.ok(jdbcOperations.query(sql, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
		}));
	}

	@PostMapping(URL + "/query/object")
	public ResponseEntity<?> queryForObject(@RequestBody SqlAndObjsDto sqlAndObjsDto) {
		return ResponseEntity.ok(jdbcOperations.queryForObject(
				sqlAndObjsDto.getSql(),
				applicationContext.getBean(JobInstanceRowMapper.class),
				sqlAndObjsDto.getArgs()));
	}

	@PostMapping(URL + "/query/rse")
	public ResponseEntity<?> query(@RequestBody SqlAndObjsStartCountDto sqlAndObjsStartCountDto) {
		int start = sqlAndObjsStartCountDto.getStart();
		int count = sqlAndObjsStartCountDto.getCount();
		return ResponseEntity.ok(
				jdbcOperations.query(
						sqlAndObjsStartCountDto.getSql(),
						sqlAndObjsStartCountDto.getArgs(),
						new ResultSetExtractor<List<JobInstance>>() {
							private List<JobInstance> list = new ArrayList<>();

							@Override
							public List<JobInstance> extractData(ResultSet rs) throws SQLException, DataAccessException {
								int rowNum = 0;
								while (rowNum < start && rs.next()) {
									rowNum++;
								}
								while (rowNum < start + count && rs.next()) {
									RowMapper<JobInstance> rowMapper =
											applicationContext.getBean(JobInstanceRowMapper.class);
									list.add(rowMapper.mapRow(rs, rowNum));
									rowNum++;
								}
								return list;
							}
						}
				)
		);
	}

	@PostMapping(URL + "/object/query")
	public ResponseEntity<?> queryForObjectQuery(@RequestBody SqlAndObjsDto sqlAndObjsDto) {
		return ResponseEntity.ok(jdbcOperations.queryForObject(
				sqlAndObjsDto.getSql(),
				sqlAndObjsDto.getArgs(),
				applicationContext.getBean(JobInstanceRowMapper.class)
		));
	}

	@PostMapping(URL + "/query/object/req-type")
	public ResponseEntity<?> queryForObjectReqType(@RequestBody SqlReqTypeArgsDto sqlReqTypeArgsDto) {
		return ResponseEntity.ok(jdbcOperations.queryForObject(
				sqlReqTypeArgsDto.getSql(),
				sqlReqTypeArgsDto.getReqType(),
				sqlReqTypeArgsDto.getArgs()
		));
	}

}
