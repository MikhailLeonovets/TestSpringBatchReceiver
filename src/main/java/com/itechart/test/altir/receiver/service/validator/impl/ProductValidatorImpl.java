package com.itechart.test.altir.receiver.service.validator.impl;

import com.itechart.test.altir.receiver.repository.entity.Product;
import com.itechart.test.altir.receiver.service.validator.ProductValidator;
import com.itechart.test.altir.receiver.service.validator.model.ValidatorResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@PropertySource("classpath:/message/validation.properties")
public class ProductValidatorImpl implements ProductValidator {
	@Value("${data.validated.msg}")
	private String dataValidatedMsg;
	@Value("${null.data.input.msg}")
	private String nullDataInputMsg;
	@Value("${product.count.must.positive.msg}")
	private String idMustBePositive;
	@Value("${product.name.blank.msg}")
	private String blankNameMsg;
	@Value("${product.price.must.positive.msg}")
	private String priceMustBePositive;
	@Value("${id.must.positive.msg}")
	private String countMustBePositive;

	private final Boolean validated = true;
	private final Boolean notValidated = false;

	@Override
	public ValidatorResponse validate(Product product) {
		ValidatorResponse validatorResponseName = validateName(product.getName());
		ValidatorResponse validatorResponsePrice = validatePrice(product.getPrice());
		ValidatorResponse validatorResponseCount = validateCount(product.getCount());
		if (!validatorResponseName.getIsValidated()) {
			return validatorResponseName;
		}
		if (!validatorResponsePrice.getIsValidated()) {
			return validatorResponsePrice;
		}
		if (!validatorResponseCount.getIsValidated()) {
			return validatorResponseCount;
		}
		return new ValidatorResponse(validated, dataValidatedMsg);
	}

	@Override
	public ValidatorResponse validateId(Long productId) {
		if (productId == null) {
			return new ValidatorResponse(notValidated, nullDataInputMsg);
		}
		if (productId <= 0L) {
			return new ValidatorResponse(notValidated, idMustBePositive);
		}
		return new ValidatorResponse(validated, dataValidatedMsg);
	}

	@Override
	public ValidatorResponse validateName(String productName) {
		if (productName == null) {
			return new ValidatorResponse(notValidated, nullDataInputMsg);
		}
		if (productName.isBlank()) {
			return new ValidatorResponse(notValidated, blankNameMsg);
		}
		return new ValidatorResponse(validated, dataValidatedMsg);
	}

	@Override
	public ValidatorResponse validatePrice(BigDecimal price) {
		if (price == null) {
			return new ValidatorResponse(notValidated, nullDataInputMsg);
		}
		if (price.compareTo(BigDecimal.ZERO) < 0) {
			return new ValidatorResponse(notValidated, priceMustBePositive);
		}
		return new ValidatorResponse(validated, dataValidatedMsg);
	}

	@Override
	public ValidatorResponse validateCount(Integer productCount) {
		if (productCount == null) {
			return new ValidatorResponse(notValidated, nullDataInputMsg);
		}
		if (productCount < 0) {
			return new ValidatorResponse(notValidated, countMustBePositive);
		}
		return new ValidatorResponse(validated, dataValidatedMsg);
	}
}
