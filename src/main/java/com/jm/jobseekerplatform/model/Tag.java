package com.jm.jobseekerplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    /**
     * user (employer) adds new tag as verified
     * if verified than invisible anyone except for admin
     * admin change visible this tag for everyone
     */
    @Column(name = "verified")
    private Boolean verified;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "vacancies_tags",
            joinColumns = @JoinColumn(name = "tags_id"),
            inverseJoinColumns = @JoinColumn(name = "vacancy_id"))
    @JsonIgnore
    private Set<Vacancy> vacancies;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "news_tags",
            joinColumns = @JoinColumn(name = "tags_id"),
            inverseJoinColumns = @JoinColumn(name = "news_id"))
    @JsonIgnore
    private Set<News> news;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "resumes_tags",
            joinColumns = @JoinColumn(name = "tags_id"),
            inverseJoinColumns = @JoinColumn(name = "resume_id"))
    @JsonIgnore
    private Set<Resume> resumes;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "profile_tags",
            joinColumns = @JoinColumn(name = "tags_id"),
            inverseJoinColumns = @JoinColumn(name = "seeker_profile_id"))
    @JsonIgnore
    private Set<SeekerProfile> seekerProfiles;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public Tag(String name, Boolean verified) {
        this.name = name;
        this.verified = verified;
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

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Set<News> getNews() {
        return news;
    }

    public void setNews(Set<News> news) {
        this.news = news;
    }

    public Set<Vacancy> getVacancies() {
        return vacancies;
    }

    public void setVacancies(Set<Vacancy> vacancies) {
        this.vacancies = vacancies;
    }

    public Set<Resume> getResumes() {
        return resumes;
    }

    public void setResumes(Set<Resume> resumes) {
        this.resumes = resumes;
    }

    public Set<SeekerProfile> getSeekerProfiles() {
        return seekerProfiles;
    }

    public void setSeekerProfiles(Set<SeekerProfile> seekerProfiles) {
        this.seekerProfiles = seekerProfiles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
