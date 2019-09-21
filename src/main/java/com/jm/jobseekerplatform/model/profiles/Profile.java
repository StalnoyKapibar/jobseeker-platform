package com.jm.jobseekerplatform.model.profiles;

import com.fasterxml.jackson.annotation.JsonValue;
import com.jm.jobseekerplatform.model.State;
import com.jm.jobseekerplatform.model.Tag;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Base64;
import java.util.Set;

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

    @Column(name = "logo")
    @Type(type = "image")
    private byte[] logo;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "profiles")
    private Set<Tag> tags;

    public Profile() {
        this.state = State.NO_ACCESS;
    }

    public Profile(byte[] logo) {
        this();
        this.logo = logo;
    }

//    @JsonValue
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

    public String getEncoderPhoto() {
        return Base64.getEncoder().encodeToString(this.getLogo());
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                '}';
    }

}
