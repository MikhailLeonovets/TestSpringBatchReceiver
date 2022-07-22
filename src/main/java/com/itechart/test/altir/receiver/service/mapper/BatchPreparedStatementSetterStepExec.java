package com.itechart.test.altir.receiver.service.mapper;

import org.springframework.batch.core.StepExecution;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.PostgresSequenceMaxValueIncrementer;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Component
@Scope("prototype")
public class BatchPreparedStatementSetterStepExec implements BatchPreparedStatementSetter {
	private Collection<StepExecution> stepExecutions;
	private Iterator<StepExecution> iterator;
	private DataFieldMaxValueIncrementer stepExecutionIncrementer = new PostgresSequenceMaxValueIncrementer();
	private int exitMessageLength = 2500;

	public BatchPreparedStatementSetterStepExec(Collection<StepExecution> stepExecutions) {
		this.stepExecutions = stepExecutions;
		iterator = stepExecutions.iterator();
	}

	public void setStepExecutions(Collection<StepExecution> stepExecutions) {
		this.stepExecutions = stepExecutions;
	}

	@Override
	public void setValues(PreparedStatement ps, int i) throws SQLException {
		StepExecution stepExecution = iterator.next();
		List<Object[]> parameters = buildStepExecutionParameters(stepExecution);
		Object[] parameterValues = parameters.get(0);
		Integer[] parameterTypes = (Integer[]) parameters.get(1);
		for (int indx = 0; indx < parameterValues.length; indx++) {
			switch (parameterTypes[indx]) {
				case Types.INTEGER:
					ps.setInt(indx + 1, (Integer) parameterValues[indx]);
					break;
				case Types.VARCHAR:
					ps.setString(indx + 1, (String) parameterValues[indx]);
					break;
				case Types.TIMESTAMP:
					if (parameterValues[indx] != null) {
						ps.setTimestamp(indx + 1, new Timestamp(((java.util.Date) parameterValues[indx]).getTime()));
					} else {
						ps.setNull(indx + 1, Types.TIMESTAMP);
					}
					break;
				case Types.BIGINT:
					ps.setLong(indx + 1, (Long) parameterValues[indx]);
					break;
				default:
					throw new IllegalArgumentException(
							"unsupported SQL parameter type for step execution field index " + i);
			}
		}
	}

	@Override
	public int getBatchSize() {
		return stepExecutions.size();
	}

	private List<Object[]> buildStepExecutionParameters(StepExecution stepExecution) {
		Assert.isNull(stepExecution.getId(),
				"to-be-saved (not updated) StepExecution can't already have an id assigned");
		Assert.isNull(stepExecution.getVersion(),
				"to-be-saved (not updated) StepExecution can't already have a version assigned");
		validateStepExecution(stepExecution);
		stepExecution.setId(stepExecutionIncrementer.nextLongValue());
		stepExecution.incrementVersion(); //Should be 0
		List<Object[]> parameters = new ArrayList<>();
		String exitDescription = truncateExitDescription(stepExecution.getExitStatus().getExitDescription());
		Object[] parameterValues = new Object[]{stepExecution.getId(), stepExecution.getVersion(),
				stepExecution.getStepName(), stepExecution.getJobExecutionId(), stepExecution.getStartTime(),
				stepExecution.getEndTime(), stepExecution.getStatus().toString(), stepExecution.getCommitCount(),
				stepExecution.getReadCount(), stepExecution.getFilterCount(), stepExecution.getWriteCount(),
				stepExecution.getExitStatus().getExitCode(), exitDescription, stepExecution.getReadSkipCount(),
				stepExecution.getWriteSkipCount(), stepExecution.getProcessSkipCount(),
				stepExecution.getRollbackCount(), stepExecution.getLastUpdated()};
		Integer[] parameterTypes = new Integer[]{Types.BIGINT, Types.INTEGER, Types.VARCHAR, Types.BIGINT,
				Types.TIMESTAMP, Types.TIMESTAMP, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER,
				Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER,
				Types.INTEGER, Types.TIMESTAMP};
		parameters.add(0, Arrays.copyOf(parameterValues, parameterValues.length));
		parameters.add(1, Arrays.copyOf(parameterTypes, parameterTypes.length));
		return parameters;
	}

	private void validateStepExecution(StepExecution stepExecution) {
		Assert.notNull(stepExecution, "stepExecution is required");
		Assert.notNull(stepExecution.getStepName(), "StepExecution step name cannot be null.");
		Assert.notNull(stepExecution.getStartTime(), "StepExecution start time cannot be null.");
		Assert.notNull(stepExecution.getStatus(), "StepExecution status cannot be null.");
	}

	private String truncateExitDescription(String description) {
		if (description != null && description.length() > exitMessageLength) {
			return description.substring(0, exitMessageLength);
		} else {
			return description;
		}
	}
}
