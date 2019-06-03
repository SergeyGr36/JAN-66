package com.ra.course.janus.traintickets.entity;

public class Invoice {
	private long id;
	private double price;
	private String attributes;


	public Invoice(long id, double price, String attributes) {
//		super();
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

	public double getPrice() {
//		double priceForKm = 10.2;
//
//		price = priceForKm * getDistance();
		return price;
	}

	public void setPrice(double p) {
		price = p;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String a) {
		attributes = a;
	}

//	private double getDistance() {
//		double distance;
//		distance = Math.random() * 100;
//		return distance;
//	}
}
