package com.jm.jobseekerplatform.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "portfolios")
public class Portfolio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "link")
    private String link;

    @Column(name = "description", columnDefinition = "mediumtext")
    private String description;

    public Portfolio() {
    }

    public Portfolio(String link, String description) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Portfolio portfolio = (Portfolio) o;

        if (id != null ? !id.equals(portfolio.id) : portfolio.id != null) return false;
        if (link != null ? !link.equals(portfolio.link) : portfolio.link != null) return false;
        return description != null ? description.equals(portfolio.description) : portfolio.description == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
