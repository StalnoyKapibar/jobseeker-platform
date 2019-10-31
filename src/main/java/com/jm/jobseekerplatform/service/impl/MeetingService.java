package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.interfaces.MeetingDao;
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
    private MeetingDao meetingDao;

    public void addMeeting(Meeting meeting){
        meetingDao.save(meeting);
    }

    public Meeting getMeetingById(Long id){
        return meetingDao.getOne(id);
    }

    public void updateMeeting(Meeting toUpdate) {
        meetingDao.save(toUpdate);
    }

    public void updateMeetingsOnPassing() {
        List<Meeting> meetings = meetingDao.findAllByStatus_PassedIsNot();
        if (meetings != null) {
            LocalDateTime dateTime = LocalDateTime.now();
            for (Meeting meeting : meetings) {
                if (dateTime.isAfter(meeting.getDate())) {
                    meeting.setStatus(Status.PASSED);
                    meetingDao.save(meeting);
                }
            }
        }
    }
}
