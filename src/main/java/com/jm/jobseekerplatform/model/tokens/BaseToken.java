package com.jm.jobseekerplatform.model.tokens;

import com.jm.jobseekerplatform.model.users.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public  class BaseToken implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @OneToOne(fetch = FetchType.EAGER)
    private User user;

    @Column(name = "expiry_date", nullable = false)
    private Date expiryDate;

    public BaseToken() {
    }

    public BaseToken(String token, User user, Date expiryDate) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
