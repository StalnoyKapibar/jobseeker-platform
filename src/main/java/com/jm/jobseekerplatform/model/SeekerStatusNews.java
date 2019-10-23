package com.jm.jobseekerplatform.model;

import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "seeker_status_news")
public class SeekerStatusNews implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private LocalDateTime date;

    @OneToOne(fetch = FetchType.LAZY)
    private SeekerProfile seeker;

    @OneToOne(fetch = FetchType.LAZY)
    private News news;

    @Column(name = "state", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private NewsStatus newsStatus;

    public SeekerStatusNews() {}

    public SeekerStatusNews(LocalDateTime date, SeekerProfile seeker, News news, NewsStatus newsStatus) {
        this.date = date;
        this.news = news;
        this.seeker = seeker;
        this.newsStatus = newsStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public SeekerProfile getSeeker() {
        return seeker;
    }

    public void setSeeker(SeekerProfile seeker) {
        this.seeker = seeker;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public NewsStatus getNewsStatus() {
        return newsStatus;
    }

    public void setNewsStatus(NewsStatus newsStatus) {
        this.newsStatus = newsStatus;
    }
}
