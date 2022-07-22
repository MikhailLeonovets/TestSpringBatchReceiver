package com.itechart.test.altir.receiver.service.mapper;


import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StepExecutionRowMapperSpecial implements RowMapper {
	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		Long jobExecutionId = rs.getLong(18);
		JobExecution jobExecution = new JobExecution(jobExecutionId);
		jobExecution.setStartTime(rs.getTimestamp(19));
		jobExecution.setEndTime(rs.getTimestamp(20));
		jobExecution.setStatus(BatchStatus.valueOf(rs.getString(21)));
		jobExecution.setExitStatus(new ExitStatus(rs.getString(22), rs.getString(23)));
		jobExecution.setCreateTime(rs.getTimestamp(24));
		jobExecution.setLastUpdated(rs.getTimestamp(25));
		jobExecution.setVersion(rs.getInt(26));
		return new StepExecutionRowMapper(jobExecution).mapRow(rs, rowNum);
	}
}
