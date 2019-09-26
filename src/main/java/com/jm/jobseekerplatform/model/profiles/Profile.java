package com.jm.jobseekerplatform.model.profiles;

import com.jm.jobseekerplatform.model.State;
import com.jm.jobseekerplatform.model.users.User;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Base64;
import java.util.Date;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Profile<T extends User> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "state", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private State state;

    @Column(name = "logo")
    @Type(type = "image")
    private byte[] logo;

    @Column(name = "expiry_block")
    private Date expiryBlock;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = User.class)
    private T user;

    public Profile() {
        this.state = State.NO_ACCESS;
    }

    public Profile(byte[] logo) {
        this();
        this.logo = logo;
    }
    public Profile(T user){
        this.user = user;
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

    public Date getExpiryBlock() {
        return expiryBlock;
    }

    public void setExpiryBlock(Date expiryBlock) {
        this.expiryBlock = expiryBlock;
    }

    public String getEncoderPhoto() {
        return Base64.getEncoder().encodeToString(this.getLogo());
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public T getUser() {
        return user;
    }

    public void setUser(T user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                '}';
    }

}
