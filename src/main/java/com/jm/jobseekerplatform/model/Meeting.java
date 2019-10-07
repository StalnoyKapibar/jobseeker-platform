package com.jm.jobseekerplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.io.Serializable;

@Entity
public class Meeting implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="seeker_profile_id")
    @JsonIgnore
    private SeekerProfile seekerProfile;

    @ManyToOne
    @JoinColumn(name="vacancy_id")
    @JsonIgnore
    private Vacancy vacancy;

    @Column(name = "meeting_date")
    private LocalDateTime date;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status;

    public Meeting() {
    }

    public Meeting(SeekerProfile seekerProfile, Vacancy vacancy, LocalDateTime date, Status status) {
        this.seekerProfile = seekerProfile;
        this.vacancy = vacancy;
        this.date = date;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SeekerProfile getSeekerProfile() {
        return seekerProfile;
    }

    public void setSeekerProfile(SeekerProfile seekerProfile) {
        this.seekerProfile = seekerProfile;
    }

    public Vacancy getVacancy() {
        return vacancy;
    }

    public void setVacancy(Vacancy vacancy) {
        this.vacancy = vacancy;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "id=" + id +
                ", seekerProfile=" + seekerProfile +
                ", vacancy='" + vacancy + '\'' +
                ", date='" + date + '\'' +
                ", status=" + status +
                '}';
    }
}
