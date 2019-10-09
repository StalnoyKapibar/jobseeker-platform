package com.jm.jobseekerplatform.model.chats;

import com.jm.jobseekerplatform.model.Resume;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.users.User;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class ChatWithTopicResume extends ChatWithTopic<Resume> {
    public ChatWithTopicResume() {
    }

    public ChatWithTopicResume(Profile creatorProfile, Resume about) {
        super(creatorProfile, about);
    }

    public ChatWithTopicResume(Profile creatorProfile, List<User> chatMembers, Resume topic) {
        super(creatorProfile, chatMembers, topic);
    }
}
