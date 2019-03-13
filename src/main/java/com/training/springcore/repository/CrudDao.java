package com.training.springcore.repository;

import java.util.List;

public interface CrudDao<T,ID> {

    void persist(T element);

    // Read
    T findById(ID id);

    List<T> findAll();
    // Update

    void delete(T element);
}
