package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.PointDAO;
import com.jm.jobseekerplatform.model.Point;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.lang.Math.*;

@Service("pointService")
@Transactional
public class PointService extends AbstractService<Point> {

    @Autowired
    private PointDAO dao;

    public List<Point> sortPointsByDistance(Point point, List<Point> points) {

        final float EARTH_RADIUS = (float) 6371.;

        Map<Float, Point> sortPoints = new TreeMap<>();

        for ( Point p : points) {
            final float dlng = deg2rad(p.getLongitudeX() - point.getLongitudeX());
            final float dlat = deg2rad(p.getLatitudeY() - point.getLatitudeY());
            final float a = (float) (Math.pow(sin(dlat / 2), 2) + cos(deg2rad(point.getLatitudeY())) * cos(deg2rad(p.getLatitudeY())) * Math.pow(sin(dlng / 2),2));
            final float c = (float) (2 * atan2(sqrt(a), sqrt(1 - a)));
            float distance =  c * EARTH_RADIUS;
            sortPoints.put(distance, p);
        }

        List<Point> sortListPoints = new ArrayList<>();

        for(Map.Entry<Float,Point> entry : sortPoints.entrySet()) {
            Point value = entry.getValue();
            sortListPoints.add(value);
        }

        return sortListPoints;
    }

    private static float deg2rad(final float degree) {
        return (float) (degree * (Math.PI / 180));
    }
}
