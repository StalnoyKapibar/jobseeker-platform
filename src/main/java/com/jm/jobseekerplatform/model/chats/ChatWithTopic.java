package com.jm.jobseekerplatform.model.chats;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.jm.jobseekerplatform.model.createdByProfile.CreatedByProfile;
import com.jm.jobseekerplatform.model.profiles.Profile;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@MappedSuperclass
public class ChatWithTopic<T extends CreatedByProfile> extends Chat {

    @ManyToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private T topic;


    public ChatWithTopic(Profile creatorProfile, T about) {
        super(creatorProfile);
        this.topic = about;
    }

    public ChatWithTopic() {
    }

    public T getTopic() {
        return topic;
    }
}
