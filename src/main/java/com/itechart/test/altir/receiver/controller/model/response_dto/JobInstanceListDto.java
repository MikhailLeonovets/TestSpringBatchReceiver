package com.itechart.test.altir.receiver.controller.model.response_dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.batch.core.JobInstance;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class JobInstanceListDto {
	private List<JobInstance> jobInstances;
}
