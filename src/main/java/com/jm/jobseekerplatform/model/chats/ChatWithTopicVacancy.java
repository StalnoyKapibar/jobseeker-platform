package com.jm.jobseekerplatform.model.chats;

import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.model.Vacancy;

import javax.persistence.Entity;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@Entity
public class ChatWithTopicVacancy extends ChatWithTopic<Vacancy> {
    public ChatWithTopicVacancy() {
    }

    public ChatWithTopicVacancy(Profile creator, Vacancy about) {
        super(creator, about);
    }
}
