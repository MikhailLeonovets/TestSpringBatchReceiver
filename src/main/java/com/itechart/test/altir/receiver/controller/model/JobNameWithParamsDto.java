package com.itechart.test.altir.receiver.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.JobParameters;

@Getter
@Setter
@AllArgsConstructor
public class JobNameWithParamsDto {
	private String jobName;
	private JobParameters jobParameters;
}
