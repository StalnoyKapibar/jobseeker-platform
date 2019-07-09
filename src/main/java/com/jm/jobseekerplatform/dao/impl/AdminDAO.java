package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.users.UserAdmin;
import org.springframework.stereotype.Repository;

@Repository("adminDAO")
public class AdminDAO extends AbstractDAO<UserAdmin> {
}
