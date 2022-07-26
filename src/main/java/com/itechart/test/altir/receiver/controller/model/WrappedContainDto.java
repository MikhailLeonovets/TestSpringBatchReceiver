package com.itechart.test.altir.receiver.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WrappedContainDto implements Serializable {
	private Class<?> aClass;
	private Object value;
}
