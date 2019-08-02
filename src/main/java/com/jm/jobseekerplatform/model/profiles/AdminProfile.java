package com.jm.jobseekerplatform.model.profiles;

import javax.persistence.Entity;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@Entity
public class AdminProfile extends Profile {
    public AdminProfile() {
    }

    @Override
    public String getNameForUi() {
        return "Admin";
    }

    @Override
    public String getTypeForUi() {
        return "Администратор";
    }
}
