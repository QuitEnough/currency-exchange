package ru.yana.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<T, ID> {

    Optional<T> findById(ID id);
    List<T> findAll();
    T save(T entity);
    Optional<T> update(T entity);
    void delete(ID id);

}
