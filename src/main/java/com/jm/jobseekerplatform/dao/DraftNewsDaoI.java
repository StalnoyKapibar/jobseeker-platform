package com.jm.jobseekerplatform.dao;

import com.jm.jobseekerplatform.model.DraftNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DraftNewsDaoI extends JpaRepository<DraftNews,Long> {

    @Query("SELECT COUNT(id) FROM DraftNews dn WHERE dn.isValid IS NULL AND dn.original.id=:newsId")
    long getOnValidation(long newsId);

    @Query("SELECT dn FROM DraftNews dn WHERE dn.isValid IS NULL ORDER BY dn.id DESC")
    List<DraftNews> findAllActive();
}
