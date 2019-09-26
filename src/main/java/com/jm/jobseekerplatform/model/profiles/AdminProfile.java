package com.jm.jobseekerplatform.model.profiles;

import com.jm.jobseekerplatform.model.users.AdminUser;
import com.jm.jobseekerplatform.model.users.EmployerUser;

import javax.persistence.Entity;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@Entity
public class AdminProfile extends Profile<AdminUser> {

    public AdminProfile() {
    }
    public AdminProfile(AdminUser user) {
        super(user);
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
