package com.jm.jobseekerplatform.dao.interfaces;

import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.SeekerStatusNews;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public  interface SeekerStatusNewsDao extends JpaRepository<SeekerStatusNews, Long> {

    List<SeekerStatusNews> getAllSeekerStatusNewsDAO(SeekerProfile seekerProfile);

    SeekerStatusNews getSeekerStatusNewsDAO(News news, SeekerProfile seekerProfile);

}
