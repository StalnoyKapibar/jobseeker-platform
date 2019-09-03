package com.jm.jobseekerplatform.service.impl.users;

import com.jm.jobseekerplatform.dao.AdminUserDaoI;
import com.jm.jobseekerplatform.dao.impl.users.AdminUserDAO;
import com.jm.jobseekerplatform.model.users.AdminUser;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("adminService")
@Transactional
public class AdminUserService extends AbstractService<AdminUser> {

    @Autowired
    private AdminUserDaoI adminUserDaoI;

    @Autowired
    private AdminUserDAO adminUserDAO;

    public Page<AdminUser> findAll(Pageable pageable) {
        return adminUserDaoI.findAll(pageable);
    }

    public AdminUser getByProfileId(Long adminProfileId) {
        return adminUserDAO.getByProfileId(adminProfileId);
    }
}
