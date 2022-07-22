package com.itechart.test.altir.receiver.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class SqlArgsReqTypesDto implements Serializable {
	private String sql;
	private Object[] args;
	private Class<?> reqType;
}
