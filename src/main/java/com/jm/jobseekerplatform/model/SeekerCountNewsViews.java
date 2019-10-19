package com.jm.jobseekerplatform.model;

import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "seeker_count_news_views")
public class SeekerCountNewsViews implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private LocalDateTime date;

    @OneToOne(fetch = FetchType.LAZY)
    private SeekerProfile seeker;

    @OneToOne(fetch = FetchType.LAZY)
    private News news;

    @Column(name = "number_views")
    private Long numberViews;

    public SeekerCountNewsViews() {}

    public SeekerCountNewsViews(LocalDateTime date, SeekerProfile seeker, News news, Long numberViews) {
        this.date        = date;
        this.news        = news;
        this.seeker      = seeker;
        this.numberViews = numberViews;
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

    public Long getNumberViews() {
        return numberViews;
    }

    public void setNumberViews(Long numberViews) {
        this.numberViews = numberViews;
    }

}
