package com.jm.jobseekerplatform.model.comments;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.profiles.Profile;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "comments")
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name ="date_time")
    private String dateTime;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Profile profile;

    public Comment() {
    }

    public Comment(String text, String dateTime) {
        this.text = text;
        this.dateTime = dateTime;
    }

    public Comment(String text, String dateTime, Profile profile) {
        this.text = text;
        this.dateTime = dateTime;
        this.profile = profile;
    }

    public Comment(String text, News news, Profile profile, String dateTime) {
        this.text = text;
        this.news = news;
        this.profile = profile;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
