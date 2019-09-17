package com.jm.jobseekerplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jm.jobseekerplatform.model.createdByProfile.CreatedByEmployerProfileBase;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "vacancies")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "vacancy")
@NamedEntityGraph(name = "vacancy-all-nodes", attributeNodes = {
        @NamedAttributeNode("creatorProfile"),
        @NamedAttributeNode("city"),
        @NamedAttributeNode("tags"),
        @NamedAttributeNode("coordinates")
})
public class Vacancy extends CreatedByEmployerProfileBase implements Serializable {

    @OneToOne(fetch = FetchType.LAZY)
    @Embedded
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

    @Column(name = "creationdate")
    private Date creationDate;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Tag> tags;

    @OneToOne(fetch = FetchType.LAZY)
    private Point coordinates;

    @Column(name = "state", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private State state;

    @Column(name = "expiry_block")
    private Date expiryBlock;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "vacancy")
    private Set<Meeting> meetings;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_profile_id", insertable = false, updatable = false)
    @JsonIgnore
    private EmployerProfile employerProfile;

    public Vacancy() {
    }

    public Vacancy(EmployerProfile employerProfile, String headline, City city, Boolean remote, String shortDescription, String description, Integer salaryMin, Integer salaryMax, Set<Tag> tags, Point coordinates, Date creationDate) {
        super(employerProfile, headline);
        this.city = city;
        this.remote = remote;
        this.shortDescription = shortDescription;
        this.description = description;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.tags = tags;
        this.coordinates = coordinates;
        state = State.NO_ACCESS;
        this.creationDate = creationDate;
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

    public Set<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(Set<Meeting> meetings) {
        this.meetings = meetings;
    }

    public EmployerProfile getEmployerProfile() {
        return employerProfile;
    }

    public void setEmployerProfile(EmployerProfile employerProfile) {
        this.employerProfile = employerProfile;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Vacancy{" +
                super.toString() +
                ", city=" + city.getName() +
                ", remote=" + remote +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", salaryMin=" + salaryMin +
                ", salaryMax=" + salaryMax +
                ", tags=" + tags +
                ", coordinates=" + coordinates +
                ", state=" + state +
                ", expiryBlock=" + expiryBlock +
                ", meetings=" + meetings +
                '}';
    }

    @Override
    public String getTypeName() {
        return "Вакансия";
    }
}
