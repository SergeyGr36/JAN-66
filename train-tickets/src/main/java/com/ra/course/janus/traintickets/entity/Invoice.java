package com.ra.course.janus.traintickets.entity;

import java.math.BigDecimal;

public class Invoice {
	private long id;
	private BigDecimal price;
	private String attributes;


	public Invoice(long id, BigDecimal price, String attributes) {
		super();
		this.id = id;
		this.price = price;
		this.attributes = attributes;
	}

	public Invoice() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal p) {
		this.price = p;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String a) {
		attributes = a;
	}

}
