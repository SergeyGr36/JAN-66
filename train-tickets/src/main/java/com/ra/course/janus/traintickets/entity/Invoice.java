package com.ra.course.janus.traintickets.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
	private long id;
	private BigDecimal price;
	private String attributes;
	}
