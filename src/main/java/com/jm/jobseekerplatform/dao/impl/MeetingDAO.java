package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.Meeting;
import com.jm.jobseekerplatform.model.Status;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("meetingDAO")
public class MeetingDAO extends AbstractDAO<Meeting> {
    public List<Meeting> getAllWihoutPassed() {
        return entityManager.createQuery("SELECT m FROM Meeting m WHERE m.status <> :status", Meeting.class)
                .setParameter("status", Status.PASSED).getResultList();
    }
}
