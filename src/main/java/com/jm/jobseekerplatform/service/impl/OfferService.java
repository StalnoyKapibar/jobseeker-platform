package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.OfferDAO;
import com.jm.jobseekerplatform.model.Offer;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("offerService")
@Transactional
public class OfferService extends AbstractService<Offer> {

    @Autowired
    @Qualifier("offerDAO")
    private OfferDAO dao;
}
