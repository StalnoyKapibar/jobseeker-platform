package com.jm.jobseekerplatform.dao.interfaces;

import com.jm.jobseekerplatform.dao.AbstractDao;
import com.jm.jobseekerplatform.dto.ResumePageDTO;
import com.jm.jobseekerplatform.model.Resume;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public interface ResumeDao extends JpaRepository<Resume, Long> {

    public Page<Resume> getAllResumes(int limit, int page);

    public Page<Resume> getPageableResumesWithFilterByQueryParamsMapAndPageNumberAndPageSize(Map<String,
            Object> queryParamsMap, int pageNumber, int pageSize);

    Page<Resume> findAllByTags(Set<Tag> tags, int limit, int page);

    public Page<Resume> getResumesSortByCity(String city, int limit, int page);

    List<Resume> findAllByTagsContains(String tagName);

    @Query("SELECT distinct e FROM Resume e where e.date between :startDate and :endDate")
    List<Resume> getResumesByDatePeriod(LocalDateTime startDate, LocalDateTime endDate);
}
