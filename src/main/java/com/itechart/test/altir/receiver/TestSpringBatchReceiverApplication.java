package com.itechart.test.altir.receiver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itechart.test.altir.receiver.service.deserializer.JobInstanceDeserializer;
import org.springframework.batch.core.JobInstance;
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
		objectMapper.registerModule(module);
		return objectMapper;
	}

	@Bean
	public Gson gson() {
		GsonBuilder builder = new GsonBuilder();
		builder.setDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		return builder.create();
	}
}
