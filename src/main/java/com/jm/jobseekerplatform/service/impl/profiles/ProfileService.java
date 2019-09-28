package com.jm.jobseekerplatform.service.impl.profiles;

import com.jm.jobseekerplatform.dao.impl.profiles.ProfileDAO;
import com.jm.jobseekerplatform.model.State;
import com.jm.jobseekerplatform.model.comments.Comment;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */
@Service("profileService")
@Transactional
public class ProfileService extends AbstractService<Profile> {

    @Autowired
    private ProfileDAO dao;

    public void blockPermanently(Profile profile) {
        profile.setState(State.BLOCK_PERMANENT);
        profile.setExpiryBlock(null);
        dao.update(profile);
    }

    public void blockTemporary(Profile profile, int periodInDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, periodInDays);
        Date expiryBlockDate = calendar.getTime();

        profile.setState(State.BLOCK_TEMPORARY);
        profile.setExpiryBlock(expiryBlockDate);
        dao.update(profile);
    }

    public void blockOwn(Profile profile) {
        profile.setState(State.BLOCK_OWN);
        profile.setExpiryBlock(null);
        dao.update(profile);
    }

    public void unblock(Profile profile) {
        profile.setState(State.ACCESS);
        profile.setExpiryBlock(null);
        dao.update(profile);
    }

    public List<Profile> loadProfilesCommentsForNews(List<Comment> comments){
        return dao.loadProfilesCommentsForNews(comments);
    }
}