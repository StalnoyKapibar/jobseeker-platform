package com.jm.jobseekerplatform.model;

import javax.persistence.Entity;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@Entity
public class ChatWithTopicVacancy extends ChatWithTopic<Vacancy> {
    public ChatWithTopicVacancy() {
    }

    public ChatWithTopicVacancy(User createdBy, Vacancy about) {
        super(createdBy, about);
    }
}
