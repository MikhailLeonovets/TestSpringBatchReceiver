package com.itechart.test.altir.receiver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itechart.test.altir.receiver.service.deserializer.JobExecutionDeserializer;
import com.itechart.test.altir.receiver.service.deserializer.JobInstanceDeserializer;
import com.itechart.test.altir.receiver.service.serializer.ExitStatusSerializer;
import com.itechart.test.altir.receiver.service.serializer.JobExecutionSerializer;
import com.itechart.test.altir.receiver.service.serializer.StepExecutionSerializer;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TestSpringBatchReceiverApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestSpringBatchReceiverApplication.class, args);
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(JobInstance.class, new JobInstanceDeserializer());
		module.addDeserializer(JobExecution.class, new JobExecutionDeserializer());
		objectMapper.registerModule(module);
		return objectMapper;
	}

	@Bean
	public Gson gson() {
		GsonBuilder builder = new GsonBuilder();
		builder.setDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		builder.registerTypeAdapter(StepExecution.class, new StepExecutionSerializer());
		builder.registerTypeAdapter(JobExecution.class, new JobExecutionSerializer());
		builder.registerTypeAdapter(ExitStatus.class, new ExitStatusSerializer());
		return builder.create();
	}
}
