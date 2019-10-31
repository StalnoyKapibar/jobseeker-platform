package com.jm.jobseekerplatform.dao.interfaces;

import com.jm.jobseekerplatform.model.DraftNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DraftNewsDao extends JpaRepository<DraftNews, Long> {

    @Query("SELECT COUNT(id) FROM DraftNews dn WHERE dn.isValid IS NULL AND dn.original.id=:newsId")
    long getOnValidation(@Param("newsId") long newsId);

    List<DraftNews> findAllByIsValidIsNullOrderByIdDesc();
}
