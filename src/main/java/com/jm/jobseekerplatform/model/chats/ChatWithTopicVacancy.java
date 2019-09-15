package com.jm.jobseekerplatform.model.chats;

import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.users.User;

import javax.persistence.Entity;
import java.util.List;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@Entity
public class ChatWithTopicVacancy extends ChatWithTopic<Vacancy> {
    public ChatWithTopicVacancy() {
    }

    public ChatWithTopicVacancy(Profile creatorProfile, Vacancy about) {
        super(creatorProfile, about);
    }

    public ChatWithTopicVacancy(Profile creatorProfile, List<User> chatMembers, Vacancy topic) {
        super(creatorProfile, chatMembers, topic);
    }
}
