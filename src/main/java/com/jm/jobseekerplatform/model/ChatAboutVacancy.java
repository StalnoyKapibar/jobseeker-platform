package com.jm.jobseekerplatform.model;

import javax.persistence.Entity;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@Entity
public class ChatAboutVacancy extends ChatAbout<Vacancy> {
    public ChatAboutVacancy() {
    }

    public ChatAboutVacancy(User createdBy, Vacancy about) {
        super(createdBy, about);
    }
}
