package com.jm.jobseekerplatform.model.comments;


import com.jm.jobseekerplatform.model.profiles.SeekerProfile;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name= "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "text")
    private String text;

    @ManyToOne
    private SeekerProfile seekerProfile;

    public Comment(){

    }

    public Comment(String text, SeekerProfile seekerProfile) {
        this.text = text;
        this.seekerProfile = seekerProfile;
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

    public SeekerProfile getSeekerProfile() {
        return seekerProfile;
    }

    public void setSeekerProfile(SeekerProfile seekerProfile) {
        this.seekerProfile = seekerProfile;
    }
}
