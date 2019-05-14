package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.PortfolioDAO;
import com.jm.jobseekerplatform.model.Portfolio;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("portfolioService")
@Transactional
public class PortfolioService extends AbstractService<Portfolio> {

    @Autowired
    private PortfolioDAO dao;
}
