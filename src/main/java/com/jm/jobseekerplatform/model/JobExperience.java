package com.jm.jobseekerplatform.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "JobExperience")
public class JobExperience implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstWorkDay")
    private Date firstWorkDay;

    @Column(name = "lastWorkDay")
    private Date lastWorkDay;

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

    public JobExperience(Date firstWorkDay, Date lastWorkDay, String companyName, String position, String responsibilities) {
        this.firstWorkDay = firstWorkDay;
        this.lastWorkDay = lastWorkDay;
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

    public Date getFirstWorkDay() {
        return firstWorkDay;
    }

    public void setFirstWorkDay(Date firstWorkDay) {
        this.firstWorkDay = firstWorkDay;
    }

    public Date getLastWorkDay() {
        return lastWorkDay;
    }

    public void setLastWorkDay(Date lastWorkDay) {
        this.lastWorkDay = lastWorkDay;
    }
}
