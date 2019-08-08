package com.jm.jobseekerplatform.model.profiles;

import com.fasterxml.jackson.annotation.JsonValue;
import com.jm.jobseekerplatform.model.EmployerReviews;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class EmployerProfile extends Profile implements Serializable {

    @Column(name = "companyname")
    private String companyName;

    @Column(name = "website")
    private String website;

    @Column(name = "description", columnDefinition = "mediumtext")
    private String description;

    @Column(name = "logo")
    @Type(type = "image")
    private byte[] logo;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id")
    private Set<EmployerReviews> reviews;

    @Column(name = "expiry_block")
    private Date expiryBlock;

    @Column(name = "publication_position")
    private int publicationPosition;

    public EmployerProfile() {
    }

    public EmployerProfile(String companyName, String website, String description, byte[] logo) {
        super();
        this.companyName = companyName;
        this.website = website;
        this.description = description;
        this.logo = logo;
    }

    @JsonValue
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

    public Set<EmployerReviews> getReviews() {
        return reviews;
    }

    public void setReviews(Set<EmployerReviews> reviews) {
        this.reviews = reviews;
    }

    public int getPublicationPosition(){
        return publicationPosition;
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
            return reviews.stream().mapToInt(EmployerReviews::getEvaluation).average().orElse(0);
        } else {
            return 0d;
        }

    }

    public Date getExpiryBlock() {
        return expiryBlock;
    }

    public void setExpiryBlock(Date expiryBlock) {
        this.expiryBlock = expiryBlock;
    }
}
