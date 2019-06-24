package com.jm.jobseekerplatform.model;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@MappedSuperclass
public class ChatWithTopic<T extends CreatedByUser> extends Chat {

    @ManyToOne
    private T topic;


    public ChatWithTopic(User createdBy, T about) {
        super(createdBy);
        this.topic = about;
    }

    public ChatWithTopic() {
    }

    public T getTopic() {
        return topic;
    }
}
