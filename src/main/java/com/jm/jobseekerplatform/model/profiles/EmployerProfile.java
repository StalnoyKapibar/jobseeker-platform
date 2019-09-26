package com.jm.jobseekerplatform.model.profiles;

import com.jm.jobseekerplatform.model.EmployerReviews;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.model.users.EmployerUser;
import com.jm.jobseekerplatform.model.users.SeekerUser;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

@Entity
public class EmployerProfile extends Profile<EmployerUser>  implements Serializable {

    @Column(name = "companyname")
    private String companyName;

    @Column(name = "website")
    private String website;

    @Column(name = "description", columnDefinition = "mediumtext")
    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id")
    private Set<EmployerReviews> reviews;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_profile_id")
    private Set<Vacancy> vacancies;

    public EmployerProfile() {
    }

    @Override
    public String getFullName() {
        return companyName;
    }

    @Override
    public String getTypeName() {
        return "Работодатель";
    }

    public EmployerProfile(String companyName, String website, String description, byte[] logo) {
        super(logo);
        this.companyName = companyName;
        this.website = website;
        this.description = description;
    }

    public String getCompanyName() {
        return companyName;
    }

    @Override
    public Long getId() {
        return super.getId();
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
        if (reviews != null) {
            BigDecimal bd = new BigDecimal(Double.toString(reviews.stream().mapToInt(EmployerReviews::getEvaluation).average().orElse(0)));
            bd = bd.setScale(1, RoundingMode.HALF_UP);
            return bd.doubleValue();
        } else {
            return 0d;
        }

    }

    public Set<Vacancy> getVacancies() {
        return vacancies;
    }

    public void setVacancies(Set<Vacancy> vacancies) {
        this.vacancies = vacancies;
    }
}
