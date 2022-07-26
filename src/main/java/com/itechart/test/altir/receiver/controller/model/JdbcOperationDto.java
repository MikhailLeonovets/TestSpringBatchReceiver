package com.itechart.test.altir.receiver.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JdbcOperationDto implements Serializable {
	private String methodName;
	private Collection<ImmutablePair<Class<?>, Object>> arguments;
}
