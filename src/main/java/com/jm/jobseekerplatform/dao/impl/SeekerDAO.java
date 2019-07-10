package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.users.SeekerUser;
import org.springframework.stereotype.Repository;

@Repository("seekerDAO")
public class SeekerDAO extends AbstractDAO<SeekerUser> {
}
