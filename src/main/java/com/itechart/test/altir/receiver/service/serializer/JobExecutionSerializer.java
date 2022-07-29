package com.itechart.test.altir.receiver.service.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import lombok.SneakyThrows;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JobExecutionSerializer implements JsonSerializer<JobExecution> {
	@SneakyThrows
	@Override
	public JsonElement serialize(JobExecution jobExecution, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("id", jobExecution.getId());
		jsonObject.addProperty("startTime", dateToString(jobExecution.getStartTime()));
		jsonObject.addProperty("endTime", dateToString(jobExecution.getEndTime()));
		jsonObject.addProperty("status", jobExecution.getStatus().toString());
		jsonObject.add("exitStatus", new ExitStatusSerializer().serialize(jobExecution.getExitStatus(),
				ExitStatus.class, jsonSerializationContext));
		jsonObject.addProperty("createTime", dateToString(jobExecution.getCreateTime()));
		jsonObject.addProperty("lastUpdated", dateToString(jobExecution.getLastUpdated()));
		jsonObject.addProperty("version", jobExecution.getVersion());
		jsonObject.add
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
