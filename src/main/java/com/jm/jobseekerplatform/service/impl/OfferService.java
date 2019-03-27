package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.OfferDAO;
import com.jm.jobseekerplatform.model.Offer;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;

public class OfferService extends AbstractService<Offer> {

    @Autowired
    private OfferDAO dao;
}
