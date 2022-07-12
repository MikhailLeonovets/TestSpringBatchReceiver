package com.itechart.test.altir.receiver.repository.entity;

import com.itechart.test.altir.receiver.repository.entity.mapped_superclass.Identity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
public class Product extends Identity {
	@Column(name = "name", nullable = false, unique = true)
	private String name;
	@Column(name = "price", nullable = false)
	private BigDecimal price;
	@Column(name = "count", nullable = false)
	private Integer count;
}
