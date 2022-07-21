package com.itechart.test.altir.receiver.service.mapper;

import org.springframework.batch.core.JobInstance;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Scope("prototype")
public class JobInstanceRowMapper implements RowMapper<JobInstance> {

	@Override
	public JobInstance mapRow(ResultSet rs, int rowNum) throws SQLException {
		JobInstance jobInstance = new JobInstance(rs.getLong(1), rs.getString(2));
		// should always be at version=0 because they never get updated
		jobInstance.incrementVersion();
		return jobInstance;
	}
}
