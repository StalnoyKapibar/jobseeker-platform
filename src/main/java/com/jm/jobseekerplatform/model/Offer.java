package com.jm.jobseekerplatform.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "offers")
public class Offer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "place", nullable = false)
    private String place;

    @Column(name = "salary", nullable = false)
    private Integer salary;

    public Offer() {
    }

    public Offer(String companyName, String position, String place, Integer salary) {
        this.companyName = companyName;
        this.position = position;
        this.place = place;
        this.salary = salary;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }
}
