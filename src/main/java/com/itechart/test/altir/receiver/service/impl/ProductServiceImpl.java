package com.itechart.test.altir.receiver.service.impl;

import com.itechart.test.altir.receiver.repository.ProductRepository;
import com.itechart.test.altir.receiver.repository.entity.Product;
import com.itechart.test.altir.receiver.service.ProductService;
import com.itechart.test.altir.receiver.service.exception.DataInputException;
import com.itechart.test.altir.receiver.service.exception.ProductException;
import com.itechart.test.altir.receiver.service.validator.ProductValidator;
import com.itechart.test.altir.receiver.service.validator.model.ValidatorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@PropertySource("classpath:/message/exception.properties")
public class ProductServiceImpl implements ProductService {
	private final ProductRepository productRepository;
	private final ProductValidator productValidator;

	@Value("${id.product.not.found.msg}")
	private String productIdNotFoundMsg;
	@Value("${product.name.already.exists.msg}")
	private String productNameAlreadyExistsMsg;

	@Autowired
	public ProductServiceImpl(ProductRepository productRepository, ProductValidator productValidator) {
		this.productRepository = productRepository;
		this.productValidator = productValidator;
	}

	@Override
	public Product save(Product product) throws DataInputException, ProductException {
		ValidatorResponse validatorResponse = productValidator.validate(product);
		if (!validatorResponse.getIsValidated()) {
			throw new DataInputException(validatorResponse.getMessage());
		}
		if (productRepository.existsByName(product.getName())) {
			throw new ProductException(String.format(productNameAlreadyExistsMsg, product.getName()));
		}
		return productRepository.save(product);
	}

	@Override
	public List<Product> findAll() {
		return productRepository.findAll();
	}

	@Override
	public Product findById(Long id) throws ProductException {
		return productRepository.findById(id)
				.orElseThrow(() -> new ProductException(String.format(productIdNotFoundMsg, id)));
	}

	@Override
	public Product update(Product product) throws DataInputException, ProductException {
		ValidatorResponse validatorResponseProduct = productValidator.validate(product);
		if (!validatorResponseProduct.getIsValidated()) {
			throw new DataInputException(validatorResponseProduct.getMessage());
		}
		ValidatorResponse validatorResponseId = productValidator.validateId(product.getId());
		if (!validatorResponseId.getIsValidated()) {
			throw new DataInputException(validatorResponseId.getMessage());
		}
		if (!productRepository.existsById(product.getId())) {
			throw new ProductException(String.format(productIdNotFoundMsg, product.getId()));
		}
		return productRepository.save(product);
	}

	@Override
	public void deleteById(Long id) throws DataInputException, ProductException {
		ValidatorResponse validatorResponseId = productValidator.validateId(id);
		if (!validatorResponseId.getIsValidated()) {
			throw new DataInputException(validatorResponseId.getMessage());
		}
		if (!productRepository.existsById(id)) {
			throw new ProductException(String.format(productIdNotFoundMsg, id));
		}
		productRepository.deleteById(id);
	}
}
