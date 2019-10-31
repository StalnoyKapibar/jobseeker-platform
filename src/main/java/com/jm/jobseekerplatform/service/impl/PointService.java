package com.jm.jobseekerplatform.service.impl;
import com.jm.jobseekerplatform.dao.interfaces.PointDao;
import com.jm.jobseekerplatform.model.Point;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.Math.*;

@Service("pointService")
@Transactional
public class PointService extends AbstractService<Point> {

    @Autowired
    private PointDao pointDao;

    final float EARTH_RADIUS = (float) 6371.;

    private static float deg2rad(final float degree) {
        return (float) (degree * (Math.PI / 180));
    }

    public float getDistance(Point point1, Point point2) {
        final float dlng = deg2rad(point1.getLongitudeX() - point2.getLongitudeX());
        final float dlat = deg2rad(point1.getLatitudeY() - point2.getLatitudeY());
        final float a = (float) (Math.pow(sin(dlat / 2), 2) + cos(deg2rad(point2.getLatitudeY())) * cos(deg2rad(point1.getLatitudeY())) * Math.pow(sin(dlng / 2),2));
        final float c = (float) (2 * atan2(sqrt(a), sqrt(1 - a)));
        return  c * EARTH_RADIUS;
    }
}
