package com.itechart.test.altir.receiver.service.mapper;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
@Scope("prototype")
public class JobExecutionRowMapper implements RowMapper<JobExecution> {
	private JobInstance jobInstance;

	public void setJobInstance(JobInstance jobInstance) {
		this.jobInstance = jobInstance;
	}

	private JobParameters jobParameters;
	private JdbcTemplate jdbcTemplate;

	public JobExecutionRowMapper() {
	}

	public JobExecutionRowMapper(JobInstance jobInstance) {
		this.jobInstance = jobInstance;
	}

	@Override
	public JobExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
		Long id = rs.getLong(1);
		String jobConfigurationLocation = rs.getString(10);
		JobExecution jobExecution;
		jobParameters = getJobParameters(id);
		if (jobInstance == null) {
			jobExecution = new JobExecution(id, jobParameters, jobConfigurationLocation);
		} else {
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

	private JobParameters getJobParameters(Long executionId) {
		final Map<String, JobParameter> map = new HashMap<>();
		RowCallbackHandler handler = new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				JobParameter.ParameterType type = JobParameter.ParameterType.valueOf(rs.getString(3));
				JobParameter value = null;

				if (type == JobParameter.ParameterType.STRING) {
					value = new JobParameter(rs.getString(4),
							rs.getString(8).equalsIgnoreCase("Y"));
				} else if (type == JobParameter.ParameterType.LONG) {
					long longValue = rs.getLong(6);
					value = new JobParameter(rs.wasNull() ? null : longValue,
							rs.getString(8).equalsIgnoreCase("Y"));
				} else if (type == JobParameter.ParameterType.DOUBLE) {
					double doubleValue = rs.getDouble(7);
					value = new JobParameter(rs.wasNull() ? null : doubleValue, rs.getString(8).equalsIgnoreCase("Y"));
				} else if (type == JobParameter.ParameterType.DATE) {
					value = new JobParameter(rs.getTimestamp(5), rs.getString(8).equalsIgnoreCase("Y"));
				}

				// No need to assert that value is not null because it's an enum
				map.put(rs.getString(2), value);
			}
		};

		jdbcTemplate.query("SELECT JOB_EXECUTION_ID, KEY_NAME, TYPE_CD, "
						+ "STRING_VAL, DATE_VAL, LONG_VAL, DOUBLE_VAL, IDENTIFYING from BATCH_JOB_EXECUTION_PARAMS where JOB_EXECUTION_ID = ?",
				new Object[]{executionId}, handler);

		return new JobParameters(map);
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
