package com.jm.jobseekerplatform.model.chats;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.jm.jobseekerplatform.model.createdByProfile.CreatedByProfile;
import com.jm.jobseekerplatform.model.createdByProfile.CreatedByProfileBase;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.users.User;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.List;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@Entity
public class ChatWithTopic<T extends CreatedByProfile> extends Chat {

    @ManyToOne(targetEntity = CreatedByProfileBase.class)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private T topic;


    public ChatWithTopic(Profile creatorProfile, T about) {
        super(creatorProfile);
        this.topic = about;
    }

    public ChatWithTopic(Profile creatorProfile, List<User> chatMembers, T topic) {
        super(creatorProfile, chatMembers);
        this.topic = topic;
    }

    public ChatWithTopic() {
    }

    public T getTopic() {
        return topic;
    }
}
