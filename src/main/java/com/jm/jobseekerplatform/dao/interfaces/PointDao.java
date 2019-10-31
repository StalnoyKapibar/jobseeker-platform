package com.jm.jobseekerplatform.dao.interfaces;

import com.jm.jobseekerplatform.dao.AbstractDao;
import com.jm.jobseekerplatform.model.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointDao extends JpaRepository<Point, Long> {
}
