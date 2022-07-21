package com.itechart.test.altir.receiver.controller;

import com.itechart.test.altir.receiver.controller.model.UserDetails;
import com.itechart.test.altir.receiver.controller.model.WrappedContainDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

//@RestController
public class RemoteDataSourceController {
	private static final String URL = "/remote-data-source";

	private final DataSource dataSource;

	@Autowired
	public RemoteDataSourceController(DataSource dataSource) {
		this.dataSource = dataSource;
	}


	@GetMapping(URL + "/connection")
	public ResponseEntity<?> getConnection() throws SQLException {
		return ResponseEntity.ok(dataSource.getConnection());
	}

	@GetMapping(URL + "/connection/user-details")
	public ResponseEntity<?> getConnection(@RequestBody UserDetails userDetails) throws SQLException {
		return ResponseEntity.ok(dataSource.getConnection(userDetails.getUsername(), userDetails.getPassword()));
	}

	@GetMapping(URL + "/log-writer")
	public ResponseEntity<?> getLogWriter() throws SQLException {
		return ResponseEntity.ok(dataSource.getLogWriter());
	}

	@PostMapping(URL + "/log-writer")
	private ResponseEntity<?> setLogWriter(@RequestBody PrintWriter printWriter) throws SQLException {
		dataSource.setLogWriter(printWriter);
		return ResponseEntity.ok("OK");
	}

	@PostMapping(URL + "/login-timeout")
	public ResponseEntity<?> setLoginTimeOut(@RequestBody int timeout) throws SQLException {
		dataSource.setLoginTimeout(timeout);
		return ResponseEntity.ok("OK");
	}

	@GetMapping(URL + "/logger")
	public ResponseEntity<?> getParentLogger() throws SQLFeatureNotSupportedException {
		return ResponseEntity.ok(dataSource.getParentLogger());
	}

	@GetMapping(URL + "/unwrapper")
	public ResponseEntity<?> unwrap(@RequestBody Class<?> aClass) throws SQLException {
		return ResponseEntity.ok(dataSource.unwrap(aClass));
	}

	@GetMapping(URL + "/wrapper-state")
	public ResponseEntity<?> isWrapperFor(@RequestBody Class<?> aClass) throws SQLException {
		return ResponseEntity.ok(dataSource.isWrapperFor(aClass));
	}

}
