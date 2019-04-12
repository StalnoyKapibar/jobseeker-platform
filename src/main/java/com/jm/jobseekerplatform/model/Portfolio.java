package com.jm.jobseekerplatform.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "portfolios")
public class Portfolio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "projectname")
    private String projectName;

    @Column(name = "link")
    private String link;

    @Column(name = "description", columnDefinition = "mediumtext")
    private String description;

    public Portfolio() {
    }

    public Portfolio(String projectName, String link, String description) {
        this.projectName = projectName;
        this.link = link;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Portfolio portfolio = (Portfolio) o;
        return Objects.equals(id, portfolio.id) &&
                Objects.equals(projectName, portfolio.projectName) &&
                Objects.equals(link, portfolio.link) &&
                Objects.equals(description, portfolio.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectName, link, description);
    }
}
