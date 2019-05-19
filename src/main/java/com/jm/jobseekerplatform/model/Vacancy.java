package com.jm.jobseekerplatform.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "vacancies")
public class Vacancy implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "headline", nullable = false)
    private String headline;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "isremote", nullable = false)
    private Boolean isRemote;

    @Column(name = "shortdescription")
    private String shortDescription;

    @Column(name = "description", nullable = false, columnDefinition = "mediumtext")
    private String description;

    @Column(name = "salarymin")
    private Integer salaryMin;

    @Column(name = "salarymax")
    private Integer salaryMax;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Tag> tags;

    @Column(name = "state", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private State state;

    @Column(name = "expiry_block")
    private Date expiryBlock;

    public Vacancy() {
    }

    public Vacancy(String headline, String city, Boolean isRemote, String shortDescription, String description, Integer salaryMin, Integer salaryMax, Set<Tag> tags, State state) {
        this.headline = headline;
        this.city = city;
        this.isRemote = isRemote;
        this.shortDescription = shortDescription;
        this.description = description;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.tags = tags;
        this.state = state;
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

    public Integer getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(Integer salaryMin) {
        this.salaryMin = salaryMin;
    }

    public Integer getSalaryMax() {
        return salaryMax;
    }

    public void setSalaryMax(Integer salaryMax) {
        this.salaryMax = salaryMax;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getRemote() {
        return isRemote;
    }

    public void setRemote(Boolean remote) {
        isRemote = remote;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Date getExpiryBlock() {
        return expiryBlock;
    }

    public void setExpiryBlock(Date expiryBlock) {
        this.expiryBlock = expiryBlock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vacancy vacancy = (Vacancy) o;
        return Objects.equals(id, vacancy.id) &&
                Objects.equals(headline, vacancy.headline) &&
                Objects.equals(city, vacancy.city) &&
                Objects.equals(isRemote, vacancy.isRemote) &&
                Objects.equals(shortDescription, vacancy.shortDescription) &&
                Objects.equals(description, vacancy.description) &&
                Objects.equals(salaryMin, vacancy.salaryMin) &&
                Objects.equals(salaryMax, vacancy.salaryMax) &&
                Objects.equals(tags, vacancy.tags) &&
                Objects.equals(state, vacancy.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, headline, city, isRemote, shortDescription, description, salaryMin, salaryMax, tags, state);
    }
}
