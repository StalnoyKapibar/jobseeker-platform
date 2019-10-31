package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.interfaces.NewsDao;
import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.Subscription;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("newsService")
@Transactional
public class NewsService extends AbstractService<News> {

    @Autowired
    private NewsDao newsDao;

    public Page<News> getAllByEmployerProfileId(EmployerProfile employerProfile, Pageable pageable) {
        return newsDao.getAllByEmployerProfileId(employerProfile, pageable);
    }

    public Page<News> getAllBySeekerProfileTags(SeekerProfile profile, Pageable pageable) {
        return newsDao.getBySeekerProfileTags(profile, pageable);
    }

    public Page<News> getAllBySubscriptions(Iterable<Subscription> subscriptions, Pageable pageable) {
        return newsDao.getBySubscriptions(subscriptions, pageable);
    }
}
