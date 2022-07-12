package com.itechart.test.altir.receiver.service;

import com.itechart.test.altir.receiver.repository.entity.Product;

import java.util.List;

public interface ProductService {
	Product save(Product product);

	List<Product> findAll();

	Product findById(Long id);

	Product update(Product product);

	void deleteById(Long id);
}
