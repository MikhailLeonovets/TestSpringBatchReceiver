package com.itechart.test.altir.receiver.controller.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.item.ExecutionContext;

@Getter
@Setter
public class ExecutionContextDto {
	private Long executionId;
	private String stepName;
	private ExecutionContext executionContext;

}
