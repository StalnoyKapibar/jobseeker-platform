package com.jm.jobseekerplatform.model.comments;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jm.jobseekerplatform.model.profiles.Profile;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name= "comments")
public class Comment implements Serializable {
    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

    @Column(name = "text")
    private String text;


    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id")
    private Profile author;


    public Comment() {
    }

    public Comment(String text, Profile author) {
        this.text = text;
        this.author = author;
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

    public Profile getAuthor() {
        return author;
    }

    public void setAuthor(Profile author) {
        this.author = author;
    }

}
