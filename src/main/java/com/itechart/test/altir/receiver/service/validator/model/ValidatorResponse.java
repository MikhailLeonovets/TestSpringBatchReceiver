package com.itechart.test.altir.receiver.service.validator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ValidatorResponse {
	private Boolean isValidated;
	private String message;
}
