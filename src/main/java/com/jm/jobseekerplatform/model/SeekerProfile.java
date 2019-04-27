package com.jm.jobseekerplatform.model;

import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "seekerprofiles")
public class SeekerProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "mediumtext")
    private String description;

    @Column(name = "photo")
    @Type(type = "image")
    private byte[] photo;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Tag> tags;

    @OneToMany(mappedBy = "seekerProfile", fetch = FetchType.EAGER)
    private Set<EmployerReviews> reviews;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Portfolio> portfolios;

    public SeekerProfile() {
    }

    public SeekerProfile(String name, String description, byte[] photo, Set<Tag> tags, Set<Portfolio> portfolios) {
        this.name = name;
        this.description = description;
        this.photo = photo;
        this.tags = tags;
        this.portfolios = portfolios;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public Set<Portfolio> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(Set<Portfolio> portfolios) {
        this.portfolios = portfolios;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<EmployerReviews> getReviews() {
        return reviews;
    }

    public void setReviews(Set<EmployerReviews> reviews) {
        this.reviews = reviews;
    }
}
