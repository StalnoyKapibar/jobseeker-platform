package com.jm.jobseekerplatform.model;

import javax.persistence.*;
import java.io.Serializable;

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

    @Column(name = "description", nullable = false, columnDefinition = "mediumtext")
    private String description;

    @Column(name = "salarymin")
    private Integer salaryMin;

    @Column(name = "salarymax")
    private Integer salaryMax;

    public Vacancy() {
    }

    public Vacancy(String headline, String city, Boolean isRemote, String description, Integer salaryMin, Integer salaryMax) {
        this.headline = headline;
        this.city = city;
        this.isRemote = isRemote;
        this.description = description;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vacancy vacancy = (Vacancy) o;

        if (id != null ? !id.equals(vacancy.id) : vacancy.id != null) return false;
        if (headline != null ? !headline.equals(vacancy.headline) : vacancy.headline != null) return false;
        if (city != null ? !city.equals(vacancy.city) : vacancy.city != null) return false;
        if (isRemote != null ? !isRemote.equals(vacancy.isRemote) : vacancy.isRemote != null) return false;
        if (description != null ? !description.equals(vacancy.description) : vacancy.description != null) return false;
        if (salaryMin != null ? !salaryMin.equals(vacancy.salaryMin) : vacancy.salaryMin != null) return false;
        return salaryMax != null ? salaryMax.equals(vacancy.salaryMax) : vacancy.salaryMax == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (headline != null ? headline.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (isRemote != null ? isRemote.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (salaryMin != null ? salaryMin.hashCode() : 0);
        result = 31 * result + (salaryMax != null ? salaryMax.hashCode() : 0);
        return result;
    }
}
