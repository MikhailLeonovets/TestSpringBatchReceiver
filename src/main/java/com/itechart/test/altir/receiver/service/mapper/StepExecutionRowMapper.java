package com.itechart.test.altir.receiver.service.mapper;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Scope("prototype")
public class StepExecutionRowMapper implements RowMapper<StepExecution> {
	private JobExecution jobExecution;

	public StepExecutionRowMapper() {

	}

	public StepExecutionRowMapper(JobExecution jobExecution) {
		this.jobExecution = jobExecution;
	}

	@Override
	public StepExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
		StepExecution stepExecution = new StepExecution(rs.getString(2), jobExecution, rs.getLong(1));
		stepExecution.setStartTime(rs.getTimestamp(3));
		stepExecution.setEndTime(rs.getTimestamp(4));
		stepExecution.setStatus(BatchStatus.valueOf(rs.getString(5)));
		stepExecution.setCommitCount(rs.getInt(6));
		stepExecution.setReadCount(rs.getInt(7));
		stepExecution.setFilterCount(rs.getInt(8));
		stepExecution.setWriteCount(rs.getInt(9));
		stepExecution.setExitStatus(new ExitStatus(rs.getString(10), rs.getString(11)));
		stepExecution.setReadSkipCount(rs.getInt(12));
		stepExecution.setWriteSkipCount(rs.getInt(13));
		stepExecution.setProcessSkipCount(rs.getInt(14));
		stepExecution.setRollbackCount(rs.getInt(15));
		stepExecution.setLastUpdated(rs.getTimestamp(16));
		stepExecution.setVersion(rs.getInt(17));
		return stepExecution;
	}
}
