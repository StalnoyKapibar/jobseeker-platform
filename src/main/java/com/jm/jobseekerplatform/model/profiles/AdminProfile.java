package com.jm.jobseekerplatform.model.profiles;

import com.jm.jobseekerplatform.model.users.AdminUser;

import javax.persistence.Entity;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@Entity
public class AdminProfile extends Profile<AdminUser> {

    public AdminProfile() {
    }

    @Override
    public String getFullName() {
        return "Admin";
    }

    @Override
    public String getTypeName() {
        return "Администратор";
    }
}
