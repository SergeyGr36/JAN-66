package com.ra.janus.developersteam.interfaces;

import java.util.List;

import com.ra.janus.developersteam.entity.Developer;

public interface IDeveloperDAO<T extends Developer> {

	List<T> getAll();

	T getById(long id);

	boolean update(T entity);

	boolean delete(long id);

	boolean save(T entity);
}