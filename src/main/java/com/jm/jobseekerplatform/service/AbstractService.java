package com.jm.jobseekerplatform.service;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.VerificationToken;
import com.jm.jobseekerplatform.service.impl.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.io.Serializable;
import java.util.List;

@Transactional
public abstract class AbstractService <T extends Serializable> {

    @Autowired
    private AbstractDAO<T> abstractDAO;

    @Autowired
    VerificationTokenService tokenService;

    public void add(T entity) {
        abstractDAO.add(entity);
    }

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
        return (T) abstractDAO.getById(id);
    }

    public void delete(T entity) {
        abstractDAO.delete(entity);
    }

    public void deleteById(Long id) {
        VerificationToken token = tokenService.findeByUserId(id);
        if(token!=null){
            tokenService.delete(token);
        }

        abstractDAO.deleteById(id);

    }
}
