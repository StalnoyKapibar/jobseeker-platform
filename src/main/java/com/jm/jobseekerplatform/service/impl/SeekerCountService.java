package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.SeekerCountDAOI;
import com.jm.jobseekerplatform.dao.impl.SeekerCountDAO;
import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.SeekerCountNewsViews;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SeekerCountService extends AbstractService<SeekerCountNewsViews> {

    @Autowired
    SeekerCountDAO seekerCountDAO;

    @Autowired
    SeekerCountDAOI seekerCountDAOI;

    public void updateAllSeekerCount(List<SeekerCountNewsViews> list) {
        seekerCountDAOI.saveAll(list);
    }

    public List<SeekerCountNewsViews> getAllSeekerCount(SeekerProfile seekerProfile) {
        return seekerCountDAO.getAllSeekerCountDAO(seekerProfile);
    }

    public SeekerCountNewsViews getSeekerCount(News news) {
        return seekerCountDAO.getSeekerCountDAO(news);
    }

    public void update(SeekerCountNewsViews seekerCountNewsViews) {
        seekerCountDAO.update(seekerCountNewsViews);
    }

}
