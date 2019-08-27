package com.jm.jobseekerplatform.model.chats;

import com.jm.jobseekerplatform.model.Resume;
import com.jm.jobseekerplatform.model.profiles.Profile;

import javax.persistence.Entity;

@Entity
public class ChatWithTopicResume extends ChatWithTopic<Resume> {
    public ChatWithTopicResume() {
    }

    public ChatWithTopicResume(Profile creatorProfile, Resume about) {
        super(creatorProfile, about);
    }
}
