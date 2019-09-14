package com.jm.jobseekerplatform.model.users;

import com.fasterxml.jackson.annotation.*;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = EmployerUser.class, name = "employer"),
        @JsonSubTypes.Type(value = SeekerUser.class, name = "seeker"),
        @JsonSubTypes.Type(value = AdminUser.class, name = "admin")
})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "users")
public abstract class User<T extends Profile> implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private char[] password;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = Profile.class)
    private T profile;

    @ManyToOne(fetch = FetchType.EAGER)
    private UserRole authority;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "confirm", nullable = false)
    private boolean confirm;

    public User() {
    }

    public User(String email, char[] password, LocalDateTime date, UserRole authority, T profile) {
        this.email = email;
        this.password = password;
        this.date = date;
        this.authority = authority;
        this.enabled = true;
        this.confirm = false;
        this.profile = profile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    public T getProfile() {
        return profile;
    }

    public void setProfile(T profile) {
        this.profile = profile;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(authority);
    }

    public UserRole getAuthority() {
        return authority;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        return new String(password);
    }

    public char[] getPasswordChar() {
        return password;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String getUsername() {
        return email;
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
        return confirm && enabled;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User<?> user = (User<?>) o;
        return enabled == user.enabled &&
                confirm == user.confirm &&
                Objects.equals(id, user.id) &&
                Objects.equals(email, user.email) &&
                Arrays.equals(password, user.password) &&
                Objects.equals(date, user.date);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, email, date, enabled, confirm);
        result = 31 * result + Arrays.hashCode(password);
        return result;
    }
}
