package com.jm.jobseekerplatform.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "vacancies")
public class Vacancy implements Serializable, CreatedByEmployerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "employer_profile_id")
    private EmployerProfile employerProfile;

    @Column(name = "headline", nullable = false)
    private String headline;

    @OneToOne (fetch = FetchType.LAZY,cascade = CascadeType.MERGE)//LAZY)
    private City city;

    @Column(name = "remote", nullable = false)
    private Boolean remote;

    @Column(name = "shortdescription")
    private String shortDescription;

    @Column(name = "description", nullable = false, columnDefinition = "mediumtext")
    private String description;

    @Column(name = "salarymin")
    private Integer salaryMin;

    @Column(name = "salarymax")
    private Integer salaryMax;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.REMOVE)
    private Set<Tag> tags;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    private Point coordinates;

    @Column(name = "state", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private State state;

    @Column(name = "expiry_block")
    private Date expiryBlock;

    public Vacancy() {
    }

    public Vacancy(EmployerProfile employerProfile, String headline, City city, Boolean remote, String shortDescription, String description, Integer salaryMin, Integer salaryMax, Set<Tag> tags, Point coordinates) {
        this.employerProfile = employerProfile;
        this.headline = headline;
        this.city = city;
        this.remote = remote;
        this.shortDescription = shortDescription;
        this.description = description;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.tags = tags;
        this.coordinates = coordinates;
        state = State.NO_ACCESS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    public EmployerProfile getEmployerProfile() {
        return employerProfile;
    }

    public void setEmployerProfile(EmployerProfile employerProfile) {
        this.employerProfile = employerProfile;
    }

    @JsonIgnore
    @Override
    public EmployerProfile getCreator() {
        return employerProfile;
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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Boolean getRemote() {
        return remote;
    }

    public void setRemote(Boolean remote) {
        this.remote = remote;
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

    public Point getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return "Vacancy{" + "\n" +
                "id=" + id +
                "," +"\n" + "employerProfile=" + employerProfile +
                "," +"\n" + " headline='" + headline +
                "," +"\n" + " city=" + city.getName() +
                "," +"\n" + " remote=" + remote +
                "," +"\n" + " shortDescription='" + shortDescription +
                "," +"\n" + " description='" + description +
                "," +"\n" + " salaryMin=" + salaryMin +
                "," +"\n" + " salaryMax=" + salaryMax +
                "," +"\n" + " tags=" + tags +
                "," +"\n" + " coordinates=" + coordinates +
                "," +"\n" + " state=" + state +
                "," +"\n" + " expiryBlock=" + expiryBlock +
                '}';
    }
}
