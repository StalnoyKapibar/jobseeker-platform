package com.jm.jobseekerplatform.model;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "city")
public class City implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "point", nullable = false)
    private Point centerPoint;

    @OneToMany(fetch = FetchType.EAGER)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(id, city.id) &&
                Objects.equals(name, city.name) &&
                Objects.equals(centerPoint, city.centerPoint) &&
                Objects.equals(cityDistances, city.cityDistances);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, centerPoint, cityDistances);
    }
}
