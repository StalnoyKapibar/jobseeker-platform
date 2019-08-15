package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.City;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CityDAO extends AbstractDAO<City> {

    public City findCityByName(String name) {
        List<City> resultList = entityManager.createQuery("select c from City c join c.point where c.name=:city").setParameter("city", name)
                .setHint("org.hibernate.cacheable", true).getResultList();
        if (resultList.size() == 0) {
            return null;
        } else if (resultList.size() == 1) {
            return resultList.get(0);
        }

        throw new RuntimeException("Several cities!");

    }
}
