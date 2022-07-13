package com.itechart.test.altir.receiver.service;

import com.itechart.test.altir.receiver.repository.entity.Product;
import com.itechart.test.altir.receiver.service.exception.DataInputException;
import com.itechart.test.altir.receiver.service.exception.DuplicateProductException;
import com.itechart.test.altir.receiver.service.exception.ProductNotFoundException;

import java.util.List;

public interface ProductService {
	Product save(Product product) throws DataInputException, DuplicateProductException;

	List<Product> findAll();

	Product findById(Long id) throws ProductNotFoundException;

	Product findByName(String name) throws ProductNotFoundException;

	Product update(Product product) throws DataInputException, ProductNotFoundException;

	void deleteById(Long id) throws DataInputException, ProductNotFoundException;
}
