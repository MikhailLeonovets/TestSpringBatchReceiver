package com.itechart.test.altir.receiver.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.JobInstance;

@Getter
@Setter
@AllArgsConstructor
public class JobInstWithStepNameDto {
	private JobInstance jobInstance;
	private String stepName;
}
