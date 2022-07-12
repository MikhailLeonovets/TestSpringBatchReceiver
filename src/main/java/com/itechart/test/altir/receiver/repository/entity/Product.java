package com.itechart.test.altir.receiver.repository.entity;

import com.itechart.test.altir.receiver.repository.entity.mapped_superclass.Identity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends Identity {
	@Column(name = "name", nullable = false, unique = true)
	private String name;
	@Column(name = "price", nullable = false)
	private BigDecimal price;
	@Column(name = "count", nullable = false)
	private Integer count;
}
