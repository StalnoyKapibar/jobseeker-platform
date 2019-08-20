package com.jm.jobseekerplatform.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "JobExperience")
public class JobExperience implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "companyName")
    private String companyName;

    @Column(name = "position")
    private String position;

    @Column(name = "responsibilities",  columnDefinition = "mediumtext")
    private String responsibilities;

    public JobExperience() {
    }

    public JobExperience(String companyName, String position, String responsibilities) {
        this.companyName = companyName;
        this.position = position;
        this.responsibilities = responsibilities;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public Long getId() {
        return id;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }
}
