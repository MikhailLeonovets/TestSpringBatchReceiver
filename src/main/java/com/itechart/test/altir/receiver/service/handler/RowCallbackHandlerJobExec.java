package com.itechart.test.altir.receiver.service.handler;

import com.itechart.test.altir.receiver.service.mapper.JobExecutionRowMapper;
import org.springframework.batch.core.JobExecution;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Component
@Scope("prototype")
public class RowCallbackHandlerJobExec implements RowCallbackHandler {
	final Set<JobExecution> result = new HashSet<>();

	@Override
	public void processRow(ResultSet rs) throws SQLException {
		JobExecutionRowMapper mapper = new JobExecutionRowMapper();
		result.add(mapper.mapRow(rs, 0));
	}
}
