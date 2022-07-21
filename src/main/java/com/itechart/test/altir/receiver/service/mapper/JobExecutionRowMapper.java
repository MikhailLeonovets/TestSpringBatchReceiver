package com.itechart.test.altir.receiver.service.mapper;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Scope("prototype")
public class JobExecutionRowMapper implements RowMapper<JobExecution> {
	private JobInstance jobInstance;
	private JobParameters jobParameters;

	public JobExecutionRowMapper() {
	}

	public JobExecutionRowMapper(JobInstance jobInstance, JobParameters jobParameters) {
		this.jobInstance = jobInstance;
		this.jobParameters = jobParameters;
	}

	@Override
	public JobExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
		Long id = rs.getLong(1);
		String jobConfigurationLocation = rs.getString(10);
		JobExecution jobExecution;
		if (jobInstance == null) {
			jobExecution = new JobExecution(id, jobParameters, jobConfigurationLocation);
		}
		else {
			jobExecution = new JobExecution(jobInstance, id, jobParameters, jobConfigurationLocation);
		}
		jobExecution.setStartTime(rs.getTimestamp(2));
		jobExecution.setEndTime(rs.getTimestamp(3));
		jobExecution.setStatus(BatchStatus.valueOf(rs.getString(4)));
		jobExecution.setExitStatus(new ExitStatus(rs.getString(5), rs.getString(6)));
		jobExecution.setCreateTime(rs.getTimestamp(7));
		jobExecution.setLastUpdated(rs.getTimestamp(8));
		jobExecution.setVersion(rs.getInt(9));
		return jobExecution;
	}
}
