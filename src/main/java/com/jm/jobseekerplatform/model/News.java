package com.jm.jobseekerplatform.model;

import com.jm.jobseekerplatform.model.profiles.EmployerProfile;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "news")
public class News implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "headline")
    private String headline;

    @Column(name = "description", columnDefinition = "mediumtext")
    private String description;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "employerProfile_id")
    private EmployerProfile author;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    public News() {
    }

    public News(String headline, String description, EmployerProfile author, LocalDateTime date) {
        this.headline = headline;
        this.description = description;
        this.author = author;
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EmployerProfile getAuthor() {
        return author;
    }

    public void setAuthor(EmployerProfile author) {
        this.author = author;
    }
}
