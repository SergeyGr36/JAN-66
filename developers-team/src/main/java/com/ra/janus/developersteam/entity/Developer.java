package com.ra.janus.developersteam.entity;

import java.util.Objects;

public class Developer {
	long id;
	String name;

	public Developer(final long id, final String name) {

		this.id = id;
		this.name = name;
	}

	public Developer(long id, Developer developer) {
		this.id = id;
		this.name = developer.getName();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Developer developer = (Developer) o;
		return id == developer.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}