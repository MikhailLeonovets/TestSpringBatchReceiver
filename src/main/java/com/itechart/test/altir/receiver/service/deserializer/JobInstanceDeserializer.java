package com.itechart.test.altir.receiver.service.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.batch.core.JobInstance;

import java.io.IOException;

public class JobInstanceDeserializer extends StdDeserializer<JobInstance> {
	public JobInstanceDeserializer() {
		this(null);
	}

	public JobInstanceDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public JobInstance deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JsonNode node = p.getCodec().readTree(p);
		String jobName = node.get("jobName").asText();
		Long jobId = node.get("id").asLong();
		Integer jobVersion = node.get("version").asInt();
		JobInstance jobInstance = new JobInstance(jobId, jobName);
		jobInstance.setVersion(jobVersion);
		return jobInstance;
	}
}
