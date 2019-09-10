package com.jm.jobseekerplatform.service.impl.profiles;

import com.jm.jobseekerplatform.dao.impl.profiles.SeekerProfileDAO;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.model.users.SeekerUser;
import com.jm.jobseekerplatform.service.AbstractService;
import com.jm.jobseekerplatform.service.impl.users.SeekerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service("seekerProfileService")
@Transactional
public class SeekerProfileService extends AbstractService<SeekerProfile> {

    @Autowired
    private SeekerUserService seekerUserService;
    @Autowired
    private SeekerProfileDAO dao;

    public Set<SeekerProfile> getByTags(Set<Tag> tags, int limit) {

        return dao.getByTags(tags, limit);
    }
    public void updatePhoto(long id, MultipartFile file){
        SeekerUser seekerUser = seekerUserService.getById(id);
        if (!file.isEmpty()) {
            try {
                byte[] photo = file.getBytes();
                seekerUser.getProfile().setLogo(photo);
                this.update(seekerUser.getProfile());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
