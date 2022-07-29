package com.itechart.test.altir.receiver.service.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.SneakyThrows;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JobExecutionDeserializer extends StdDeserializer<JobExecution> {
	public JobExecutionDeserializer() {
		this(null);
	}

	protected JobExecutionDeserializer(Class<?> vc) {
		super(vc);
	}

	@SneakyThrows
	@Override
	public JobExecution deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JacksonException {
		JsonNode node = p.getCodec().readTree(p);
		Long id = node.get("id").asLong();
		ObjectMapper objectMapper = new ObjectMapper();
		JobParameters jobParameters = objectMapper.readValue(node.get("jobParameters").toString(), JobParameters.class);
		Date startTime;
		if (node.get("startTime") == null) {
			startTime = null;
		} else {
			startTime = dateFromString(node.get("startTime").asText());
		}
		Date endTime;
		if (node.get("endTime") == null) {
			endTime = null;
		} else {
			endTime = dateFromString(node.get("endTime").asText());
		}
		BatchStatus batchStatus = BatchStatus.valueOf(node.get("status").asText());
		ExitStatus exitStatus = deserializeExitStatus(node.get("exitStatus"));
		Date createTime = dateFromString(node.get("createTime").asText());
		Date lastUpdated = dateFromString(node.get("lastUpdated").asText());
		Integer version = node.get("version").asInt();
		String jobConfigurationName;
		if (node.get("jobConfigurationName") == null) {
			jobConfigurationName = null;
		} else {
			jobConfigurationName = node.get("jobConfigurationName").asText();
		}
		JobInstance jobInstance = deserializeJobInstance(node);
		JobExecution jobExecution;
		if (jobInstance == null) {
			jobExecution = new JobExecution(id, jobParameters, jobConfigurationName);
		} else {
			jobExecution = new JobExecution(jobInstance, id, jobParameters, jobConfigurationName);
		}
		jobExecution.setStartTime(startTime);
		jobExecution.setEndTime(endTime);
		jobExecution.setStatus(batchStatus);
		jobExecution.setExitStatus(exitStatus);
		jobExecution.setCreateTime(createTime);
		jobExecution.setLastUpdated(lastUpdated);
		jobExecution.setVersion(version);
		return jobExecution;
	}

	private JobInstance deserializeJobInstance(JsonNode node) throws IOException {
		if (node.get("jobInstance") == null || node.get("jobInstance").get("id").asText().isBlank()) {
			return null;
		}
		String jobName = node.get("jobInstance").get("jobName").asText();
		Long jobId = node.get("jobInstance").get("id").asLong();
		Integer jobVersion = node.get("jobInstance").get("version").asInt();
		JobInstance jobInstance = new JobInstance(jobId, jobName);
		jobInstance.setVersion(jobVersion);
		return jobInstance;
	}

	private Date dateFromString(String stringDate) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		return dateFormat.parse(stringDate);
	}

	private ExitStatus deserializeExitStatus(JsonNode node) {
		String exitCode = node.get("exitCode").asText();
		String exitDescription = node.get("exitDescription").asText();
		return new ExitStatus(exitCode, exitDescription);
	}
}
