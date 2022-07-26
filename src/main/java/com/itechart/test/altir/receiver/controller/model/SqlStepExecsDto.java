package com.itechart.test.altir.receiver.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.batch.core.StepExecution;

import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SqlStepExecsDto implements Serializable {
	private String sql;
	private Collection<StepExecution> stepExecutions;
}
