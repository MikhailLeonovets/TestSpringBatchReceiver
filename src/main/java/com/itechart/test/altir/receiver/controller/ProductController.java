package com.itechart.test.altir.receiver.controller;

import com.itechart.test.altir.receiver.repository.entity.Product;
import com.itechart.test.altir.receiver.service.ProductService;
import com.itechart.test.altir.receiver.service.exception.DataInputException;
import com.itechart.test.altir.receiver.service.exception.DuplicateProductException;
import com.itechart.test.altir.receiver.service.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.itechart.test.altir.receiver.controller.storage.UrlStorage.DELETE_PRODUCT_BY_ID;
import static com.itechart.test.altir.receiver.controller.storage.UrlStorage.GET_PRODUCTS_URL;
import static com.itechart.test.altir.receiver.controller.storage.UrlStorage.GET_PRODUCT_BY_ID;
import static com.itechart.test.altir.receiver.controller.storage.UrlStorage.GET_PRODUCT_BY_NAME;
import static com.itechart.test.altir.receiver.controller.storage.UrlStorage.POST_PRODUCT_URL;
import static com.itechart.test.altir.receiver.controller.storage.UrlStorage.PUT_PRODUCT_BY_ID;

@RestController
@PropertySource("classpath:/message/response.properties")
public class ProductController {
	private final ProductService productService;

	@Value("${deleted.successfully.msg}")
	private String deletedMsg;

	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping(POST_PRODUCT_URL)
	public ResponseEntity<?> create(@RequestBody Product product) throws DataInputException, DuplicateProductException {
		product = productService.save(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(product);
	}

	@GetMapping(GET_PRODUCTS_URL)
	public ResponseEntity<?> findAll() {
		return ResponseEntity.ok(productService.findAll());
	}

	@GetMapping(GET_PRODUCT_BY_ID)
	public ResponseEntity<?> findById(@PathVariable Long id) throws ProductNotFoundException {
		return ResponseEntity.ok(productService.findById(id));
	}

	@GetMapping(GET_PRODUCT_BY_NAME)
	public ResponseEntity<?> findById(@PathVariable String name) throws ProductNotFoundException {
		return ResponseEntity.ok(productService.findByName(name));
	}

	@PutMapping(PUT_PRODUCT_BY_ID)
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Product product)
			throws DataInputException, ProductNotFoundException {
		return ResponseEntity.ok(productService.update(product));
	}

	@DeleteMapping(DELETE_PRODUCT_BY_ID)
	public ResponseEntity<?> deleteById(@PathVariable Long id) throws DataInputException, ProductNotFoundException {
		productService.deleteById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(deletedMsg);
	}
}
