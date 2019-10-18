package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.CityDAO;
import com.jm.jobseekerplatform.dao.impl.CityDistanceDAO;
import com.jm.jobseekerplatform.model.City;
import com.jm.jobseekerplatform.model.CityDistance;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityDistanceService extends AbstractService<CityDistance> {

    @Autowired
    private CityDistanceDAO cityDistanceDAO;

    @Autowired
    private CityDAO cityDAO;

    @Autowired
    private PointService pointService;

    public City initCityDistances(City fromCity) {
        List<City> cities = cityDAO.getAll();
        for (City toCity : cities) {
            float distance = pointService.getDistance(toCity.getPoint(), fromCity.getPoint());
            CityDistance cityDistance = new CityDistance(fromCity, toCity, distance);
            cityDistanceDAO.add(cityDistance);
            if (distance != 0) {
                cityDistanceDAO.add(new CityDistance(toCity, fromCity, distance));
            }
        }
        return fromCity;
    }

}
