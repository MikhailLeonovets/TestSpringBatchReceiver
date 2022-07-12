package com.itechart.test.altir.receiver.service;

import com.itechart.test.altir.receiver.repository.entity.Product;
import com.itechart.test.altir.receiver.service.exception.DataInputException;
import com.itechart.test.altir.receiver.service.exception.ProductException;

import java.util.List;

public interface ProductService {
	Product save(Product product) throws DataInputException, ProductException;

	List<Product> findAll();

	Product findById(Long id) throws ProductException;

	Product update(Product product) throws DataInputException, ProductException;

	void deleteById(Long id) throws DataInputException, ProductException;
}
