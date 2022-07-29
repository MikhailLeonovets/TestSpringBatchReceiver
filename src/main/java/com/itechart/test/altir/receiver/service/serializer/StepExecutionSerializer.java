package com.itechart.test.altir.receiver.service.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import lombok.SneakyThrows;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StepExecutionSerializer implements JsonSerializer<StepExecution> {
	@SneakyThrows
	@Override
	public JsonElement serialize(StepExecution value, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("stepName", value.getStepName());
		jsonObject.addProperty("id", value.getId());
		jsonObject.addProperty("startTime", dateToString(value.getStartTime()));
		jsonObject.addProperty("endTime", dateToString(value.getEndTime()));
		jsonObject.addProperty("status", value.getStatus().toString());
		jsonObject.addProperty("commitCount", value.getCommitCount());
		jsonObject.addProperty("readCount", value.getReadCount());
		jsonObject.addProperty("filterCount", value.getFilterCount());
		jsonObject.addProperty("writeCount", value.getWriteCount());
		jsonObject.addProperty("readSkipCount", value.getReadSkipCount());
		jsonObject.addProperty("writeSkipCount", value.getWriteSkipCount());
		jsonObject.addProperty("processSkipCount", value.getProcessSkipCount());
		jsonObject.addProperty("rollbackCount", value.getRollbackCount());
		jsonObject.addProperty("lastUpdated", dateToString(value.getLastUpdated()));
		jsonObject.addProperty("version", value.getVersion());
		jsonObject.add("exitStatus", new ExitStatusSerializer().serialize(value.getExitStatus(), ExitStatus.class, jsonSerializationContext));
		jsonObject.add("jobExecution", new JobExecutionSerializer().serialize(value.getJobExecution(),
				JobExecution.class, jsonSerializationContext));
		return jsonObject;
	}

	private String dateToString(Date date) throws ParseException {
		if (date == null) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		return dateFormat.format(date);
	}
}
