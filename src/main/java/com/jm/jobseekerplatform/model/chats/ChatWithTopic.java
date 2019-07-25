package com.jm.jobseekerplatform.model.chats;

import com.jm.jobseekerplatform.model.CreatedByProfile;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.users.User;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@MappedSuperclass
public class ChatWithTopic<T extends CreatedByProfile<? extends Profile>> extends Chat {

    @ManyToOne
    private T topic;


    public ChatWithTopic(Profile creator, T about) {
        super(creator);
        this.topic = about;
    }

    public ChatWithTopic() {
    }

    public T getTopic() {
        return topic;
    }
}
