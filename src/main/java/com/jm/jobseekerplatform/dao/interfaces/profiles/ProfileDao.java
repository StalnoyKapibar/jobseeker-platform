package com.jm.jobseekerplatform.dao.interfaces.profiles;

import com.jm.jobseekerplatform.model.profiles.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */
@Repository
public interface ProfileDao extends JpaRepository<Profile, Long> {

}