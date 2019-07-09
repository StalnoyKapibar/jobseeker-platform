package com.jm.jobseekerplatform.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "seeker_vacancy_record")
public class SeekerVacancyRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private LocalDateTime date;

    @OneToOne(fetch = FetchType.EAGER)
    private Seeker seeker;

    @OneToOne(fetch = FetchType.EAGER)
    private Vacancy vacancy;

    public SeekerVacancyRecord() {}

    public SeekerVacancyRecord(LocalDateTime date, Seeker seeker, Vacancy vacancy) {
        this.date = date;
        this.seeker = seeker;
        this.vacancy = vacancy;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Seeker getSeeker() {
        return seeker;
    }

    public void setSeeker(Seeker seeker) {
        this.seeker = seeker;
    }

    public Vacancy getVacancy() {
        return vacancy;
    }

    public void setVacancy(Vacancy vacancy) {
        this.vacancy = vacancy;
    }
}
