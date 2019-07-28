package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.MeetingDAO;
import com.jm.jobseekerplatform.model.Meeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("meetingService")
@Transactional
public class MeetingService {
    @Autowired
    private MeetingDAO dao;

    public void addMeeting(Meeting meeting){
        dao.add(meeting);
    }

    public Meeting getMeetingById(Long id){
        return dao.getById(id);
    }

    public void updateMeeting(Meeting toUpdate) {
        dao.update(toUpdate);
    }
}
