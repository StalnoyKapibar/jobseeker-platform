package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.CityDAO;
import com.jm.jobseekerplatform.model.City;
import com.jm.jobseekerplatform.model.Point;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class CityService extends AbstractService<City> {

    @Autowired
    private CityDAO cityDAO;

    @Autowired
    private PointService pointService;

    @Autowired
    private CityDistanceService cityDistanceService;

    public City checkCityOrGetNearest(String currentCity, Point currentPoint) {
        City city = cityDAO.findCityByName(currentCity);
        if (city!=null) {
            return city;
        } else {
            Map<Float, City> sortedCity = new TreeMap<>();
            List<City> cityList = cityDAO.getAll();
            for (City c : cityList) {
                float distance = pointService.getDistance(currentPoint, c.getPoint());
                sortedCity.put(distance, c);
            }
            return ((TreeMap<Float, City>) sortedCity).firstEntry().getValue();
        }
    }

    public City checkCityOrAdd(String currentCity, Point currentPoint) {
        City city = cityDAO.findCityByName(currentCity);
        if (city!=null) {
            return city;
        } else {
            return initCity(currentCity, currentPoint);
        }
    }

    public City initCity(String cityName, Point point) {
        City city = new City(cityName, point);
        cityDAO.add(city);
        return cityDistanceService.initCityDistances(city);
    }

    public City getCityByName(String cityName){
        return cityDAO.findCityByName(cityName);
    }
}
