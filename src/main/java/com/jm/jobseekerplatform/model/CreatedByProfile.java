package com.jm.jobseekerplatform.model;

import com.jm.jobseekerplatform.model.profiles.Profile;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */
public interface CreatedByProfile<T extends Profile> {
    T getCreator();
}
