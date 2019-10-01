package com.jm.jobseekerplatform.model.profiles;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jm.jobseekerplatform.model.*;
import com.jm.jobseekerplatform.model.comments.Comment;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
public class SeekerProfile extends Profile implements Serializable {

    @Column(name = "name")
    private String name;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "surname")
    private String surname;

    @Column(name = "description", columnDefinition = "mediumtext")
    private String description;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Tag> tags;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Portfolio> portfolios;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER )
    private Set<Vacancy> favoriteVacancy;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, mappedBy = "seekerProfile")
    @OrderBy("status, date desc")
    private Set<Meeting> meetings;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Subscription> subscriptions;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Resume> resumes;

    public SeekerProfile() {
    }

    @Override
    public String getFullName() {
        return surname + " " + name + " " + patronymic;
    }

    @Override
    public String getTypeName() {
        return "Соискатель";
    }

    public SeekerProfile(String name, String patronymic, String surname){
        this.name = name;
        this.patronymic = patronymic;
        this.surname = surname;
    }

    public SeekerProfile(String name, String patronymic, String surname, String description, byte[] photo, Set<Tag> tags,
                         Set<Portfolio> portfolios, Set<Vacancy> favoriteVacancy, Set<Subscription> subscriptions) {
        super(photo);
        this.name = name;
        this.patronymic = patronymic;
        this.surname = surname;
        this.description = description;
        this.tags = tags;
        this.portfolios = portfolios;
        this.favoriteVacancy = favoriteVacancy;
        this.subscriptions = subscriptions;
    }

    @Override
    public String getEncoderPhoto() {
        return super.getEncoderPhoto();
    }

    public Set<Vacancy> getFavoriteVacancy() {
        return favoriteVacancy;
    }

    public void setFavoriteVacancy(Set<Vacancy> favoriteVacancy) {
        this.favoriteVacancy = favoriteVacancy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Set<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Set<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(Set<Meeting> meetings) {
        this.meetings = meetings;
    }

    public Set<Resume> getResumes() {
        return resumes;
    }

    public void setResumes(Set<Resume> resumes) {
        this.resumes = resumes;
    }
}
