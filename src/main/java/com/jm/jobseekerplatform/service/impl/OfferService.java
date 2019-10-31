package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.interfaces.OfferDao;
import com.jm.jobseekerplatform.model.Offer;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("offerService")
@Transactional
public class OfferService extends AbstractService<Offer> {

    @Autowired
    private OfferDao offerDao;
}
