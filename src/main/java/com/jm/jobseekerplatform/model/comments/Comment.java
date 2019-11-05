package com.jm.jobseekerplatform.model.comments;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.Reply;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.reports.CommentReport;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "comments")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name ="date")
    private LocalDateTime dateTime;

    @Column(name = "isReply")
    private boolean isReply;

    @Column(name = "level")
    private Long level;

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

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "comment_replies", joinColumns = {@JoinColumn(name = "comment_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "replies_id", referencedColumnName = "id")})
    private Set<Reply> replies;

    public Comment() {
    }

    public Comment(String text, LocalDateTime dateTime) {
        this.text = text;
        this.dateTime = dateTime;
    }

    public Comment(String text, LocalDateTime dateTime, Profile profile) {
        this.text = text;
        this.dateTime = dateTime;
        this.profile = profile;
    }

    public Comment(String text, News news, Profile profile, LocalDateTime dateTime) {
        this.text = text;
        this.news = news;
        this.profile= profile;
        this.dateTime = dateTime;
    }

    public Comment(String text, News news, Profile profile, LocalDateTime dateTime, Long level) {
        this.text = text;
        this.news = news;
        this.profile= profile;
        this.dateTime = dateTime;
        this.level = level;
    }

    public Comment(String text, News news, Profile profile, LocalDateTime dateTime, boolean isReply) {
        this.text = text;
        this.news = news;
        this.profile= profile;
        this.dateTime = dateTime;
        this.isReply = isReply;
    }

    public Comment(String text, News news, Profile profile, LocalDateTime dateTime, boolean isReply, Long level) {
        this.text = text;
        this.news = news;
        this.profile= profile;
        this.dateTime = dateTime;
        this.isReply = isReply;
        this.level = level;
    }

    public Set<Reply> getReplies() {
        return replies;
    }

    public void setReplies(Set<Reply> replies) {
        this.replies = replies;
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Set<CommentReport> getCommentReport() {
        return commentReport;
    }

    public void setCommentReport(Set<CommentReport> commentReport) {
        this.commentReport = commentReport;
    }

    public boolean isReply() {
        return isReply;
    }

    public void setReply(boolean reply) {
        isReply = reply;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }
}
