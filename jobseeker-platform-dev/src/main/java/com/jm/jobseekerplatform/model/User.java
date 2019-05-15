package com.jm.jobseekerplatform.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public class User implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   /* @Column(name = "login", nullable = false, unique = true)
    private String login;*/

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "surname", nullable = false, unique = true)
    private String surname;


    @Column(name = "password", nullable = false)
    private char[] password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    private UserRole authority;

    public User() {
    }

    public User(String name, String surname, char[] password, String email, UserRole authority) {
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.email = email;
        this.authority = authority;
    }
    /*public User(String login, char[] password, String email, UserRole authority) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.authority = authority;
    }*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

   /* public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(authority);
    }

    public String getPassword() {
        return new String(password);
    }

    @Override
    public String getUsername() {
        return getNameAndSurname();
    }
    public String getNameAndSurname(){
        return name + " " + surname;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getAuthority() {
        return authority;
    }

    public void setAuthority(UserRole authority) {
        this.authority = authority;
    }
}
