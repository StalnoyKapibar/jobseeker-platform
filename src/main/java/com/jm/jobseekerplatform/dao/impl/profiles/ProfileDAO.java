package com.jm.jobseekerplatform.dao.impl.profiles;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.comments.Comment;
import com.jm.jobseekerplatform.model.profiles.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */
@Repository("profileDAO")
public class ProfileDAO extends AbstractDAO<Profile> {

    public List<Profile> loadProfilesCommentsForNews(List<Comment> comments){
        List<Profile> profiles = new ArrayList<>();
        for(Comment c: comments){
            Query query = entityManager.createQuery("SELECT distinct p FROM Profile p JOIN p.comments pc where pc in :commentForNews", Profile.class);
            query.setParameter("commentForNews", c);
            profiles.addAll(query.getResultList());
        }
        return profiles;
    }
}