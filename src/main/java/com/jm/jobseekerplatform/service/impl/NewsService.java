package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.NewsDaoI;
import com.jm.jobseekerplatform.dao.impl.NewsDAO;
import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.Subscription;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;

@Service("newsService")
@Transactional
public class NewsService extends AbstractService<News> {

    @Autowired
    private NewsDAO newsDAO;

    @Autowired
    private NewsDaoI newsDaoI;

    public Page<News> getAllByEmployerProfileId(EmployerProfile employerProfile, Pageable pageable) {
        return newsDaoI.getAllByEmployerProfileId(employerProfile, pageable);
    }

    public Page<News> getAllBySeekerProfileId(Set<EmployerProfile> employerProfiles, Pageable pageable) {
        return newsDaoI.getAllBySeekerProfileId(employerProfiles, pageable);
    }

    public Page<News> getAllBySubscription(Set<Subscription> subscriptions, Pageable pageable) {
        return newsDAO.getBySubscription(subscriptions, pageable);
    }
}
