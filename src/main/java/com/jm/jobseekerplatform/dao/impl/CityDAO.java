package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.City;
import org.springframework.stereotype.Repository;

@Repository
public class CityDAO extends AbstractDAO<City> {

    public City findCityByName(String name) {
        return (City)entityManager.createQuery("select c from City c where c.name=:city").setParameter("city", name).getSingleResult();
    }

}
