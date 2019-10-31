package com.jm.jobseekerplatform.dao.interfaces;

import com.jm.jobseekerplatform.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingDao extends JpaRepository<Meeting, Long> {

    List<Meeting> findAllByStatus_PassedIsNot();
}
