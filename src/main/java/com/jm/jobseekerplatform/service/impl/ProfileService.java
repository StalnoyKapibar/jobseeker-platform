package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.ProfileDAO;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */
@Service("profileService")
@Transactional
public class ProfileService extends AbstractService<Profile> {

    @Autowired
    private ProfileDAO dao;
}