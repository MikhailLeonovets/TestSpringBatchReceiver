package com.itechart.test.altir.receiver.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
public class JdbcOperationDto {
	private String methodName;
	private Collection<ImmutablePair<Class<?>, Object>> arguments;
}
