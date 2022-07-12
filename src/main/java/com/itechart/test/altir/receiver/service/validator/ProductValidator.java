package com.itechart.test.altir.receiver.service.validator;

import com.itechart.test.altir.receiver.service.validator.model.ValidatorResponse;
import com.itechart.test.altir.receiver.repository.entity.Product;

import java.math.BigDecimal;

public interface ProductValidator {
	ValidatorResponse validate(Product product);

	ValidatorResponse validateId(Long productId);

	ValidatorResponse validateName(String productName);

	ValidatorResponse validatePrice(BigDecimal price);

	ValidatorResponse validateCount(Integer productCount);

}
