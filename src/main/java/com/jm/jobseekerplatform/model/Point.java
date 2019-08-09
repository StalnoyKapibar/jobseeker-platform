package com.jm.jobseekerplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "points")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Point implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lat", nullable = false)
    private Float latitudeY;

    @Column(name = "place", nullable = false)
    private Float longitudeX;

    public Point() {
    }

    public Point(Float latitudeY, Float longitudeX) {
        this.latitudeY = latitudeY;
        this.longitudeX = longitudeX;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getLatitudeY() {
        return latitudeY;
    }

    public void setLatitudeY(Float latitudeY) {
        this.latitudeY = latitudeY;
    }

    public Float getLongitudeX() {
        return longitudeX;
    }

    public void setLongitudeX(Float longitudeX) {
        this.longitudeX = longitudeX;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Objects.equals(id, point.id) &&
                Objects.equals(latitudeY, point.latitudeY) &&
                Objects.equals(longitudeX, point.longitudeX);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, latitudeY, longitudeX);
    }
}
