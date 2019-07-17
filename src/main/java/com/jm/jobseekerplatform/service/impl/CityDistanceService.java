package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.CityDistanceDAO;
import com.jm.jobseekerplatform.model.City;
import com.jm.jobseekerplatform.model.CityDistance;
import com.jm.jobseekerplatform.model.Point;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CityDistanceService extends AbstractService<CityDistance> {

    @Autowired
    private CityDistanceDAO cityDistanceDAO;

    @Autowired
    private CityService cityService;

    @Autowired
    private PointService pointService;

    public void initCityDistances(City city) {
        Point centerPoint = city.getCenterPoint();
        pointService.add(centerPoint);
        city.setCenterPoint(centerPoint);
        cityService.add(city);
        List<City> cities = cityService.getAll();
        List<CityDistance> cityDistanceList = new ArrayList<>();

        for (int i = 0; i < cities.size(); i++) {
            Point p = cities.get(i).getCenterPoint();
            float distance = pointService.getDistance(p, city.getCenterPoint());
            CityDistance cityDistance = new CityDistance(cities.get(i), distance);
            cityDistanceDAO.add(cityDistance);
            cityDistanceList.add(cityDistance);
            if (distance!=0) {
                CityDistance cityDistance1 = new CityDistance(city, distance);
                cityDistanceDAO.add(cityDistance1);
                cities.get(i).getCityDistances().add(cityDistance1);
            }
        }
        city.setCityDistances(cityDistanceList);
        cityService.update(city);
    }
}
