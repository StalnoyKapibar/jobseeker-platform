package com.jm.jobseekerplatform.service.impl.profiles;

import com.jm.jobseekerplatform.dao.impl.profiles.SeekerProfileDAO;
import com.jm.jobseekerplatform.dto.ResumePageDTO;
import com.jm.jobseekerplatform.model.Resume;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.model.users.SeekerUser;
import com.jm.jobseekerplatform.service.AbstractService;
import org.hibernate.Hibernate;
import com.jm.jobseekerplatform.service.impl.users.SeekerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("seekerProfileService")
@Transactional
public class SeekerProfileService extends AbstractService<SeekerProfile> {

    @Autowired
    private SeekerProfileDAO dao;

    @Autowired
    private SeekerUserService seekerUserService;

    public List<SeekerProfile> getAllSeekersById(List<Resume> resumes) {
		List<Long> rez = new ArrayList<>();
		for (int i = 0; i < resumes.size(); i++) {
			SeekerProfile x = (SeekerProfile) Hibernate.unproxy(resumes.get(i).getCreatorProfile());
			rez.add(x.getId());
		}
        return dao.getAllSeekersById(rez);
    }

    public Set<SeekerProfile> getByTags(Set<Tag> tags, int limit) {
        return dao.getByTags(tags, limit);
    }

	public Page<Resume> getPageSeekerResumesById(Set<Resume> resumeSet, Long id) {
		List<Resume> resumeList = new ArrayList<>();
		resumeList.addAll(resumeSet);
		List<Long> longList = new ArrayList<>();
		longList.add(id);
		List<SeekerProfile> seekerProfile = dao.getAllSeekersById(longList);
		return new ResumePageDTO(resumeList, seekerProfile);
	}


    public void updatePhoto(long id, MultipartFile file) {
        SeekerUser seekerUser = seekerUserService.getByProfileId(id);
        if (!file.isEmpty()) {
            try {
                byte[] photo = file.getBytes();
                seekerUser.getProfile().setLogo(photo);
                update(seekerUser.getProfile());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
