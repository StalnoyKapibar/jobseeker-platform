package com.jm.jobseekerplatform.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jm.jobseekerplatform.model.comments.Comment;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "news")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class News implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "headline")
    private String headline;

    @Column(name = "description", columnDefinition = "mediumtext")
    private String description;

    @Column(name = "numberOfViews")
    private Long numberOfViews;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "employerProfile_id")
    private EmployerProfile author;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Tag> tags;

    @JsonBackReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "news", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Comment> comments;

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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Long getNumberOfViews() {
        return numberOfViews;
    }

    public void setNumberOfViews(Long numberOfViews) {
        this.numberOfViews = numberOfViews;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}
