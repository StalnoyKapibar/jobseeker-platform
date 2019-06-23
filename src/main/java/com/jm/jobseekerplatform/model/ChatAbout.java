package com.jm.jobseekerplatform.model;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@MappedSuperclass
public class ChatAbout<T> extends Chat {

    @ManyToOne
    private T about;


    public ChatAbout(User createdBy, T about) {
        super(createdBy);
        this.about = about;
    }

    public ChatAbout() {
    }

    public T getAbout() {
        return about;
    }
}
