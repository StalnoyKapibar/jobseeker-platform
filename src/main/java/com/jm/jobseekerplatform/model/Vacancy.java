package com.jm.jobseekerplatform.model;

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

    @Column(name = "headline", nullable = false)
    private String headline;

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

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Tag> tags;

    @OneToOne(fetch = FetchType.LAZY)
    private Point coordinates;

    @Column(name = "state", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private State state;

    @Column(name = "expiry_block")
    private Date expiryBlock;

    @Column(name="tracked")
    private Boolean tracked;

    public Vacancy() {
    }

    public Vacancy(EmployerProfile employerProfile, String headline, City city, Boolean remote, String shortDescription, String description, Integer salaryMin, Integer salaryMax, Set<Tag> tags, Point coordinates) {
        super(employerProfile);
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

    public Boolean getTracked() { return tracked; }

    public void setTracked(boolean tracked) { this.tracked = tracked; }

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
        return "Vacancy{" +
                super.toString() +
                ", headline='" + headline + '\'' +
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
                '}';
    }

    @Override
    public String getTypeName() {
        return "Вакансия";
    }
}
