package com.itechart.test.altir.receiver.service.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.springframework.batch.core.ExitStatus;

import java.lang.reflect.Type;

public class ExitStatusSerializer implements JsonSerializer<ExitStatus> {
	@Override
	public JsonElement serialize(ExitStatus exitStatus, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("exitCode", exitStatus.getExitCode());
		jsonObject.addProperty("exitDescription", exitStatus.getExitDescription());
		return jsonObject;
	}
}
