package com.itechart.test.altir.receiver.service.mapper;

import org.springframework.batch.core.repository.ExecutionContextSerializer;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@Component
@Scope("prototype")
public class ExecutionContextRowMapper implements RowMapper<ExecutionContext> {
	private final ExecutionContextSerializer executionContextSerializer;

	public ExecutionContextRowMapper(ExecutionContextSerializer executionContextSerializer) {
		this.executionContextSerializer = executionContextSerializer;
	}

	@Override
	public ExecutionContext mapRow(ResultSet rs, int rowNum) throws SQLException {
		ExecutionContext executionContext = new ExecutionContext();
		String serializedContext = rs.getString("SERIALIZED_CONTEXT");
		if (serializedContext == null) {
			serializedContext = rs.getString("SHORT_CONTEXT");
		}
		Map<String, Object> map;
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(serializedContext.getBytes("ISO-8859-1"));
			map = executionContextSerializer.deserialize(in);
		}
		catch (IOException ioe) {
			throw new IllegalArgumentException("Unable to deserialize the execution context", ioe);
		}
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			executionContext.put(entry.getKey(), entry.getValue());
		}
		return executionContext;
	}
}
