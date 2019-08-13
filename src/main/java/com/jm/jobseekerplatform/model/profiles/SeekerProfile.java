package com.jm.jobseekerplatform.model.profiles;

import com.jm.jobseekerplatform.model.Meeting;
import com.jm.jobseekerplatform.model.Portfolio;
import com.jm.jobseekerplatform.model.Subscription;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.model.Vacancy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Base64;
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

    @Column(name = "photo")
    @Type(type = "image")
    private byte[] photo;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Tag> tags;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Portfolio> portfolios;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Vacancy> favoriteVacancy;

    @OneToMany(cascade = CascadeType.PERSIST,
            fetch = FetchType.EAGER,
            mappedBy = "seekerProfile")
    @OrderBy("status, date desc")
    private Set<Meeting> meetings;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Subscription> subscriptions;

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

    public SeekerProfile(String name, String patronymic, String surname, String description, byte[] photo, Set<Tag> tags,
                         Set<Portfolio> portfolios, Set<Vacancy> favoriteVacancy, Set<Subscription> subscriptions) {
        this.name = name;
        this.patronymic = patronymic;
        this.surname = surname;
        this.description = description;
        this.photo = photo;
        this.tags = tags;
        this.portfolios = portfolios;
        this.favoriteVacancy = favoriteVacancy;
        this.subscriptions = subscriptions;
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

    public byte[] getPhoto() {
        return photo;
    }

    public String getEncoderPhoto() {
        return Base64.getEncoder().encodeToString(this.getPhoto());
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

}
