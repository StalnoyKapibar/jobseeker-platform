package com.jm.jobseekerplatform.service;

import com.jm.jobseekerplatform.dao.AbstractDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Transactional
public abstract class AbstractService<T extends Serializable> {

    @Autowired
    protected AbstractDao<T> abstractDao;

    public void add(T entity) {
        abstractDao.add(entity);
    }

    public void update(T entity) {
        abstractDao.update(entity);
    }

    public List<T> getAll() {
        return abstractDao.getAll();
    }

    public List<T> getAllWithLimit(int limit) {
        return abstractDao.getAllWithLimit(limit);
    }

    public T getById(Long id) {
        return abstractDao.getById(id);
    }

    public void delete(T entity) {
        abstractDao.delete(entity);
    }

    public void deleteById(Long id) {
        abstractDao.deleteById(id);
    }

    public List<T> getEntitiesByIdArray(ArrayList<Long> listId) {
        return abstractDao.getEntitiesByIdArray(listId);
    }
}
