package com.itechart.test.altir.receiver.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.batch.core.JobExecution;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SqlAndObjsMethodJobExecDto {
	private String sql;
	private Object[] args;
	private String method;
	private JobExecution jobExecution;
}
