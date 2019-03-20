package com.jm.jobseekerplatform.service;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractService <T extends Serializable> {

    @Autowired
    private AbstractDAO abstractDAO;

    public void add(T entity) {
        abstractDAO.add(entity);
    }

    public void update(T entity) {
        abstractDAO.update(entity);
    }

    public List<T> getAll() {
        return abstractDAO.getAll();
    }

    public T getById(Long id) {
        return (T) abstractDAO.getById(id);
    }

    public void delete(T entity) {
        abstractDAO.delete(entity);
    }

    public void deleteById(Long id) {
        abstractDAO.deleteById(id);
    }
}
