package com.jm.jobseekerplatform.model;

import com.jm.jobseekerplatform.model.profiles.Profile;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "vacancies_tags",
            joinColumns = @JoinColumn(name = "tags_id"),
            inverseJoinColumns = @JoinColumn(name = "vacancy_id"))
    private Set<Vacancy> vacancies;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "news_tags",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "news_id"))
    private Set<News> news;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "resumes_tags",
            joinColumns = @JoinColumn(name = "tags_id"),
            inverseJoinColumns = @JoinColumn(name = "resume_id"))
    private Set<Resume> resumes;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "profile_tags",
            joinColumns = @JoinColumn(name = "tags_id"),
            inverseJoinColumns = @JoinColumn(name = "seeker_profile_id"))
    private Set<Profile> profiles;

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

    public Set<Vacancy> getVacancies() {
        return vacancies;
    }

    public void setVacancies(Set<Vacancy> vacancies) {
        this.vacancies = vacancies;
    }

    public Set<News> getNews() {
        return news;
    }

    public void setNews(Set<News> news) {
        this.news = news;
    }

    public Set<Resume> getResumes() {
        return resumes;
    }

    public void setResumes(Set<Resume> resumes) {
        this.resumes = resumes;
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
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
