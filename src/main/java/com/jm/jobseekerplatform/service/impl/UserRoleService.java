package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.UserRoleDAO;
import com.jm.jobseekerplatform.model.UserRole;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userRoleService")
@Transactional
public class UserRoleService extends AbstractService<UserRole> {

    @Autowired
    @Qualifier("userRoleDAO")
    private UserRoleDAO dao;

    public UserRole findByAuthority(String authority) {
        return dao.findByAuthority(authority);
    }
}
