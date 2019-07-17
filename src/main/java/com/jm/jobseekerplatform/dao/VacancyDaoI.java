package com.jm.jobseekerplatform.dao;

import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.model.Vacancy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface VacancyDaoI extends JpaRepository<Vacancy,Long> {
    Page<Vacancy> findAll(Pageable pageable);

    @Query(value = "SELECT distinct v FROM Vacancy v JOIN v.tags t WHERE t IN ?1")
    Page<Vacancy> findAllByTags(Set<Tag> tags, Pageable pageable);
}
