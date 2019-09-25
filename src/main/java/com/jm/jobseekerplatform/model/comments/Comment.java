package com.jm.jobseekerplatform.model.comments;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
/*INSERT INTO jobseeker_db.comments (id, text, profile_id) values(1, 'Hello world', '8');
        INSERT INTO jobseeker_db.comments (id, text, profile_id) values(2, 'Hello java', '8');
        INSERT INTO jobseeker_db.comments (id, text, profile_id) values(3, 'It is good', '8');
        INSERT INTO jobseeker_db.comments (id, text, profile_id) values(4, 'Not bad', '8');*/
@Entity
@Table(name= "comments")
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "text")
    private String text;

   @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;


    public Comment(){

    }

    public Comment(String text, Profile profile) {
        this.text=text;
        this.profile=profile;
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

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }

}
