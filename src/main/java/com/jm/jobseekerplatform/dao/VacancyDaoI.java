package com.jm.jobseekerplatform.dao;

import com.jm.jobseekerplatform.model.Point;
import com.jm.jobseekerplatform.model.Vacancy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacancyDaoI extends JpaRepository<Vacancy, Long> {
    Page<Vacancy> findAll(Pageable pageable);

    Vacancy findVacancyByCoordinates(Point point);
}
