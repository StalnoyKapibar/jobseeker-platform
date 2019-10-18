package com.jm.jobseekerplatform.service;

import com.jm.jobseekerplatform.annotation.AccessCheck;
import com.jm.jobseekerplatform.dao.AbstractDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Transactional
public abstract class AbstractService<T extends Serializable> implements Serializable {

    @Autowired
    protected AbstractDAO<T> abstractDAO;

    public void add(T entity) {
        abstractDAO.add(entity);
    }

    @AccessCheck
    public void update(T entity) {
        abstractDAO.update(entity);
    }

    public List<T> getAll() {
        return abstractDAO.getAll();
    }

    public List<T> getAllWithLimit(int limit) {
        return abstractDAO.getAllWithLimit(limit);
    }

    public T getById(Long id) {
        return abstractDAO.getById(id);
    }

    @AccessCheck
    public void delete(T entity) {
        abstractDAO.delete(entity);
    }

    public void deleteById(Long id) {
        abstractDAO.deleteById(id);
    }

    public List<T> getEntitiesByIdArray(ArrayList<Long> listId) {
        return abstractDAO.getEntitiesByIdArray(listId);
    }

}
