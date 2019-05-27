package com.ra.janus.developersteam.dao.interfaces;

import java.util.List;

import com.ra.janus.developersteam.entity.Developer;

public interface DeveloperDAO<T extends Developer> {

	List<T> getAll();

	T getById(long id);

	boolean update(T entity);

	boolean delete(long id);

	T save(T entity);
}