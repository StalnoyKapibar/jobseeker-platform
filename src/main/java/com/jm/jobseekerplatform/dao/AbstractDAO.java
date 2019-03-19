package com.jm.jobseekerplatform.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository("abstractDAO")
public abstract class AbstractDAO {
    @PersistenceContext
    private EntityManager entityManager;


    public void add(Object o) {
        entityManager.persist(o);
    }

    public void update(Object o) {
        entityManager.merge(o);

    }


    public abstract List getAll();


    public Object getById(Long id) {
        return entityManager.find(Object.class, id);
    }


    public void deleteById(Long id) {
        Object obj = getById(id);
        entityManager.remove(obj);

    }
}
