package com.jm.jobseekerplatform.dao.interfaces;

import com.jm.jobseekerplatform.dto.ViewedVacanciesDTO;
import com.jm.jobseekerplatform.model.SeekerVacancyRecord;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeekerVacancyRecordDao extends JpaRepository<SeekerVacancyRecord, Long> {


    List<ViewedVacanciesDTO> findAllBySeeker_Id(Long seekerId);

    List<ViewedVacanciesDTO> getNumberOfViewsOffAllVacanciesByTagForSeeker(SeekerProfile seekerProfile);
}
