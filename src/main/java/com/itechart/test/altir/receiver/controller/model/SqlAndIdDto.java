package com.itechart.test.altir.receiver.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class SqlAndIdDto implements Serializable {
	private String sql;
	private Long id;
}