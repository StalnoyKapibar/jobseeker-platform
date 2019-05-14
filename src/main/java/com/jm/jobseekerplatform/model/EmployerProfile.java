package com.jm.jobseekerplatform.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employerprofiles")
public class EmployerProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "companyname")
    private String companyName;

    @Column(name = "website")
    private String website;

    @Column(name = "description", columnDefinition = "mediumtext")
    private String description;

    @Column(name = "logo")
    @Type(type = "image")
    private byte[] logo;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Vacancy> vacancies;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id")
    private Set<EmployerReviews> reviews;

    public EmployerProfile() {
    }

    public EmployerProfile(String companyName, String website, String description, byte[] logo, Set<Vacancy> vacancies) {
        this.companyName = companyName;
        this.website = website;
        this.description = description;
        this.logo = logo;
        this.vacancies = vacancies;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public Set<Vacancy> getVacancies() {
        return vacancies;
    }

    public void setVacancies(Set<Vacancy> vacancies) {
        this.vacancies = vacancies;
    }

    public Set<EmployerReviews> getReviews() {
        return reviews;
    }

    public void setReviews(Set<EmployerReviews> reviews) {
        this.reviews = reviews;
    }

    public void addNewReview(EmployerReviews employerReview) {
        if (reviews != null) {
            reviews.add(employerReview);
        } else {
            reviews = new HashSet<>();
            reviews.add(employerReview);
        }
    }

    public Double getAverageRating() {
        if (reviews != null){
            return reviews.stream().mapToInt(EmployerReviews::getEvaluation).average().orElse(0);
        }else {
            return 0d;
        }

    }
}
