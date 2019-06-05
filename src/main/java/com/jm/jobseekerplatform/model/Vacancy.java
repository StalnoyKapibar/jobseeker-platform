package com.jm.jobseekerplatform.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
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

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Tag> tags;

    @OneToOne(fetch = FetchType.EAGER)
    private Point coordinates;

    @Column(name = "state", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private State state;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<ChatMessage> chatMessages;

    @Column(name = "expiry_block")
    private Date expiryBlock;

    public Vacancy() {
    }

    public Vacancy(String headline, String city, Boolean remote, String shortDescription, String description, Integer salaryMin, Integer salaryMax, Set<Tag> tags, Point coordinates) {
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

    public void setChatMessages(Set<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public Set<ChatMessage> getChatMessages() {
        return chatMessages;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vacancy vacancy = (Vacancy) o;

        if (id != null ? !id.equals(vacancy.id) : vacancy.id != null) return false;
        if (headline != null ? !headline.equals(vacancy.headline) : vacancy.headline != null) return false;
        if (city != null ? !city.equals(vacancy.city) : vacancy.city != null) return false;
        if (remote != null ? !remote.equals(vacancy.remote) : vacancy.remote != null) return false;
        if (shortDescription != null ? !shortDescription.equals(vacancy.shortDescription) : vacancy.shortDescription != null)
            return false;
        if (description != null ? !description.equals(vacancy.description) : vacancy.description != null) return false;
        if (salaryMin != null ? !salaryMin.equals(vacancy.salaryMin) : vacancy.salaryMin != null) return false;
        if (salaryMax != null ? !salaryMax.equals(vacancy.salaryMax) : vacancy.salaryMax != null) return false;
        if (tags != null ? !tags.equals(vacancy.tags) : vacancy.tags != null) return false;
        if (coordinates != null ? !coordinates.equals(vacancy.coordinates) : vacancy.coordinates != null) return false;
        if (state != vacancy.state) return false;
        if (chatMessages !=null ? chatMessages.equals(vacancy.chatMessages) : vacancy.chatMessages != null) return false;
        return expiryBlock != null ? expiryBlock.equals(vacancy.expiryBlock) : vacancy.expiryBlock == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (headline != null ? headline.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (remote != null ? remote.hashCode() : 0);
        result = 31 * result + (shortDescription != null ? shortDescription.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (salaryMin != null ? salaryMin.hashCode() : 0);
        result = 31 * result + (salaryMax != null ? salaryMax.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (expiryBlock != null ? expiryBlock.hashCode() : 0);
        result = 31 * result + (chatMessages !=null ? chatMessages.hashCode() : 0);
        return result;
    }
}
