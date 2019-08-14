package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.MeetingDAO;
import com.jm.jobseekerplatform.model.Meeting;
import com.jm.jobseekerplatform.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

    public void updateMeetingsOnPassing() {
        List<Meeting> meetings = dao.getAllWihoutPassed();
        if(meetings != null) {
            LocalDateTime dateTime = LocalDateTime.now();
            for (Meeting meeting : meetings) {
                if (dateTime.isAfter(meeting.getDate())) {
                    meeting.setStatus(Status.PASSED);
                    dao.update(meeting);
                }
            }
        }
    }
}
