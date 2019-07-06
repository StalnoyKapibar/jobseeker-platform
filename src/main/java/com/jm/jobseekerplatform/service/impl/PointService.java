package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.PointDAO;
import com.jm.jobseekerplatform.model.Point;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("pointService")
@Transactional
public class PointService extends AbstractService<Point> {

    @Autowired
    private PointDAO dao;
}
