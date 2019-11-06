package com.jm.jobseekerplatform.model;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "city_distances")
@Where(clause = "removal_time = '1995-05-23T00:00'")
public class CityDistance implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_city_id")
    private City from;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_city_id")
    private City to;

    @Column(name = "removal_time")
    private LocalDateTime removalTime = LocalDateTime
            .of(1995, 5,23, 0,0);


    private Float distance;

    public CityDistance() {}

    public CityDistance(City from, City to, float distance) {
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public City getFrom() {
        return from;
    }

    public void setFrom(City from) {
        this.from = from;
    }

    public City getTo() {
        return to;
    }

    public void setTo(City to) {
        this.to = to;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public LocalDateTime getRemovalTime() {
        return removalTime;
    }

    public void setRemovalTime(LocalDateTime removalTime) {
        this.removalTime = removalTime;
    }
}
