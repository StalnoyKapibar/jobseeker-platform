package com.jm.jobseekerplatform.dao;

import com.jm.jobseekerplatform.model.Vacancy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface VacancyDaoI extends JpaRepository<Vacancy, Long> {

    @EntityGraph(value = "vacancy-all-nodes", type = EntityGraph.EntityGraphType.FETCH)
    Page<Vacancy> findAll(Pageable pageable);

}
