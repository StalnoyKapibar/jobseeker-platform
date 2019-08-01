package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;

@Repository
public class CityDAO extends AbstractDAO<City> {

    public City getCityByName(String name){
        return (City) entityManager.createQuery("FROM City where name=:name").setParameter("name",name).getResultList().get(0);
    }
}
