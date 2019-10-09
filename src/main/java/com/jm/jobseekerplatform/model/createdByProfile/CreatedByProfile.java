package com.jm.jobseekerplatform.model.createdByProfile;

        import com.jm.jobseekerplatform.model.profiles.Profile;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */
public interface CreatedByProfile<T extends Profile> {
    Long getId();

    T getCreatorProfile();

    String getTypeName();

    String getHeadline();
}
