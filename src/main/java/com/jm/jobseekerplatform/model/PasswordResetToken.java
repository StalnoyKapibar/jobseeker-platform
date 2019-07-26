package com.jm.jobseekerplatform.model;

import com.jm.jobseekerplatform.model.users.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken implements Serializable {

    private static final int EXPIRATION = 60 *  24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false , unique = true)
    private String token;

    @OneToOne(fetch = FetchType.EAGER)
    private User user;

    private Date expiryDate;

    public PasswordResetToken() {
    }

    public PasswordResetToken(String token, User user, Date expiryDate) {
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
