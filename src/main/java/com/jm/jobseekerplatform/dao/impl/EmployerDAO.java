package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.users.EmployerUser;
import org.springframework.stereotype.Repository;

@Repository("employerDAO")
public class EmployerDAO extends AbstractDAO<EmployerUser> {
}
