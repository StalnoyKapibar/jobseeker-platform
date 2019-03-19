package com.jm.jobseekerplatform.service;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class AbstractService {

    @Autowired
    private AbstractDAO abstractDAO;

    public void add(Object o) {
        abstractDAO.add(o);
    }


    public void update(Object o) {
        abstractDAO.update(o);
    }


    public List getAll() {
        return abstractDAO.getAll();
    }


    public Object getById(Long id) {
        return abstractDAO.getById(id);
    }


    public void deleteById(Long id) {
        abstractDAO.deleteById(id);
    }
}
