package com.jm.jobseekerplatform.model.profiles;

import com.jm.jobseekerplatform.model.State;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Profile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "state", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private State state;

    public Profile() {
        this.state = State.NO_ACCESS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public abstract String getFullName();

    public abstract String getTypeName();

    public abstract String getEncoderPhoto();

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                '}';
    }
}
