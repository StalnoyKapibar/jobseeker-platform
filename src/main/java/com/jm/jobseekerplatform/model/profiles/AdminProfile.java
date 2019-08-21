package com.jm.jobseekerplatform.model.profiles;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Base64;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@Entity
public class AdminProfile extends Profile {
    @Column(name = "logo")
    @Type(type = "image")
    private byte[] logo;

    public AdminProfile() {
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    @Override
    public String getFullName() {
        return "Admin";
    }

    @Override
    public String getTypeName() {
        return "Администратор";
    }

    @Override
    public String getEncoderPhoto() {
        return Base64.getEncoder().encodeToString(this.getLogo());
    }
}
