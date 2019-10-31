package com.jm.jobseekerplatform.dao.interfaces.profiles;

import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface SeekerProfileDao extends JpaRepository<SeekerProfile, Long> {

    Set<SeekerProfile> findAllByTags(Set<Tag> tags, int limit);

    @Query("SELECT v FROM SeekerProfile v JOIN v.resumes r WHERE r.id =:param")
    SeekerProfile findByResumeId(@Param("param") long id);

    List<SeekerProfile> findAllByExpiryBlockIsNotNull();

    @Query("SELECT u FROM User u WHERE u.profile = :profile")
    User findBySeekerProfile(@Param("profile") SeekerProfile seekerProfile);

}
