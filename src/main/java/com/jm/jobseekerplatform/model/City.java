package com.jm.jobseekerplatform.model;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "city")
public class City implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    private Point centerPoint;

    @OneToMany(fetch = FetchType.LAZY)
    private List<CityDistance> cityDistances;

    public City() {}

    public City(Point centerPoint, String name) {
        this.centerPoint = centerPoint;
        this.name = name;
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

    public Point getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(Point centerPoint) {
        this.centerPoint = centerPoint;
    }

    public List<CityDistance> getCityDistances() {
        return cityDistances;
    }

    public void setCityDistances(List<CityDistance> cityDistances) {
        this.cityDistances = cityDistances;
    }

    @JsonValue
    public String toJson(){
        return this.name;
    }
}
