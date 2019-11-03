package com.jm.jobseekerplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jm.jobseekerplatform.model.comments.Comment;
import com.jm.jobseekerplatform.model.profiles.Profile;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "replies")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Reply implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name ="date_time")
    private LocalDateTime dateTime;

    @Column(name ="address")
    private Long address;

    @Column(name="level")
    private Long level;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Profile profile;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public Reply() {
    }

    public Reply(String text, LocalDateTime dateTime, Long address, Long level, Profile profile, Comment comment) {
        this.text = text;
        this.dateTime = dateTime;
        this.address = address;
        this.level = level;
        this.profile = profile;
        this.comment = comment;
    }

    public Reply(String text, LocalDateTime dateTime, Long level, Profile profile, Comment comment) {
        this.text = text;
        this.dateTime = dateTime;
        this.level = level;
        this.profile = profile;
        this.comment = comment;
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Long getAddress() {
        return address;
    }

    public void setAddress(Long address) {
        this.address = address;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }
}
