package com.jm.jobseekerplatform.model;

import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "seekerprofiles")
public class SeekerProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "stack")
    private String stack;

    @Column(name = "description", columnDefinition = "mediumtext")
    private String description;

    @Column(name = "photo")
    @Type(type = "image")
    private byte[] photo;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Portfolio> portfolios;

    public SeekerProfile() {
    }

    public SeekerProfile(String name, String stack, String description, byte[] photo, Set<Portfolio> portfolios) {
        this.name = name;
        this.stack = stack;
        this.description = description;
        this.photo = photo;
        this.portfolios = portfolios;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public Set<Portfolio> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(Set<Portfolio> portfolios) {
        this.portfolios = portfolios;
    }
}
