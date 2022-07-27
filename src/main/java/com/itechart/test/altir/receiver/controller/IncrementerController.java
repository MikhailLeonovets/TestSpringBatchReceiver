package com.itechart.test.altir.receiver.controller;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@RestController
public class IncrementerController {
	private static final String URL = "/incrementer";

	private final DataSource dataSource;

	public IncrementerController(JdbcTemplate jdbcTemplate) {
		this.dataSource = jdbcTemplate.getDataSource();
	}

	@PostMapping(URL + "/next-key")
	public ResponseEntity<?> getNextKey(@RequestBody String sequenceQuery) {
		Connection con = DataSourceUtils.getConnection(dataSource);
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			DataSourceUtils.applyTransactionTimeout(stmt, dataSource);
			rs = stmt.executeQuery(sequenceQuery);
			if (rs.next()) {
				return ResponseEntity.ok(rs.getLong(1));
			} else {
				throw new DataAccessResourceFailureException("Sequence query did not return a result");
			}
		} catch (SQLException ex) {
			throw new DataAccessResourceFailureException("Could not obtain sequence value", ex);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(stmt);
			DataSourceUtils.releaseConnection(con, dataSource);
		}
	}
}
