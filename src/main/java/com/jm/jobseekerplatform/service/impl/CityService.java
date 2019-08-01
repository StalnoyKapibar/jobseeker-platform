package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.CityDAO;
import com.jm.jobseekerplatform.model.City;
import com.jm.jobseekerplatform.model.Point;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public String checkCityOrGetNearest(String currentCity, Point currentPoint) {
        List<City> cityList = getAll();
        String city = null;
        for (City c : cityList) {
            if (c.getName().equals(currentCity)) {
                city = c.getName();
            }
        }

        if (city!=null) {
            return city;
        } else {
            Map<Float, City> sortedCity = new TreeMap<>();
            for (City c : cityList) {
                float distance = pointService.getDistance(currentPoint, c.getCenterPoint());
                sortedCity.put(distance, c);
            }
            List<City> sortListCities = new ArrayList<>();

            for(Map.Entry<Float,City> entry : sortedCity.entrySet()) {
                City value = entry.getValue();
                sortListCities.add(value);
            }
            return sortListCities.get(0).getName();
        }
    }

    public City checkCityOrAdd(String currentCity, Point currentPoint) {
        List<City> cityList = getAll();
        City city = null;
        for (City c : cityList) {
            if (c.getName().equals(currentCity)) {
                city = c;
            }
        }

        if (city!=null) {
            return city;
        } else {
            City newCity = new City(new Point(currentPoint.getLatitudeY(), currentPoint.getLongitudeX()), currentCity);
            cityDistanceService.initCityDistances(newCity);
            return newCity;
        }
    }
    public City getCityByName(String cityName){
        return cityDAO.getCityByName(cityName);
    }
}
