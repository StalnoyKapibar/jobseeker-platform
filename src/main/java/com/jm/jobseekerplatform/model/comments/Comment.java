package com.jm.jobseekerplatform.model.comments;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.reports.CommentReport;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "comments")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Where(clause = "removal_time = '1995-05-23T00:00'")
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

    @JsonBackReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comment", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<CommentReport> commentReport;

    @Column(name = "removal_time")
    private LocalDateTime removalTime = LocalDateTime
            .of(1995, 5,23, 0,0);

    public LocalDateTime getRemovalTime() {
        return removalTime;
    }

    public void setRemovalTime(LocalDateTime removalTime) {
        this.removalTime = removalTime;
    }

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
        this.profile= profile;
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

    public Set<CommentReport> getCommentReport() {
        return commentReport;
    }

    public void setCommentReport(Set<CommentReport> commentReport) {
        this.commentReport = commentReport;
    }
}
