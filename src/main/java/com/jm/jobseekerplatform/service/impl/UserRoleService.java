package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.UserRoleDAO;
import com.jm.jobseekerplatform.model.UserRole;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRoleService extends AbstractService<UserRole> {

    @Autowired
    private UserRoleDAO dao;
}
