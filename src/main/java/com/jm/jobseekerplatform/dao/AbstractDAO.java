package com.jm.jobseekerplatform.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

@Repository("abstractDAO")
public abstract class AbstractDAO<T extends Serializable> {

    protected Class<T> clazz;

    @PersistenceContext
    protected EntityManager entityManager;

    public AbstractDAO() {
        this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void add(T entity) {
        entityManager.persist(entity);
    }

    public void update(T entity) {
        entityManager.merge(entity);
    }

    public List<T> getAll() {
        return entityManager.createQuery("SELECT e FROM " + clazz.getName() + " e", clazz).getResultList();
    }

    public List<T> getAllWithLimit(int limit) {
        return entityManager.createQuery("SELECT e FROM " + clazz.getName() + " e", clazz).setMaxResults(limit).getResultList();
    }

    public T getById(Long id) {
        return entityManager.find(clazz, id);
    }

    public void delete(T entity) {
        entityManager.remove(entity);
    }

    public void deleteById(Long id) {
        T entity = getById(id);
        delete(entity);
    }

    public List<T> getEntitiesByIdArray(ArrayList<Long> listId) {
        return entityManager.createQuery("SELECT entity FROM "
                + clazz.getName() +
                " entity where entity.id in (:ids)", clazz).setParameter("ids", listId).getResultList();
    }

}
