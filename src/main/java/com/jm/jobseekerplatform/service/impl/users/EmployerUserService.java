package com.jm.jobseekerplatform.service.impl.users;

import com.jm.jobseekerplatform.dao.EmployerUserDaoI;
import com.jm.jobseekerplatform.dao.impl.profiles.EmployerProfileDAO;
import com.jm.jobseekerplatform.dao.impl.users.EmployerUserDAO;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.users.EmployerUser;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service("employerService")
@Transactional
public class EmployerUserService extends AbstractService<EmployerUser> {

    @Autowired
    private EmployerUserDaoI employerUserDaoI;

    @Autowired
    private EmployerUserDAO employerUserDAO;

	@Autowired
	private EmployerProfileDAO employerProfileDAO;

    public Page<EmployerUser> findAll(Pageable pageable) {
        return employerUserDaoI.findAll(pageable);
    }

    public EmployerUser getByProfileId(Long employerProfileId) {
        return employerUserDAO.getByProfileId(employerProfileId);
    }

	public EmployerProfile getEmployerProfileByVacancyID(long id) {
		return employerProfileDAO.getEmployerProfileByVacancyID(id);
	}

	public List<EmployerUser> getEmployerUsersByDatePeriod(LocalDateTime startDate, LocalDateTime endDate){
        return employerUserDAO.getEmployerUsersByDatePeriod(startDate, endDate);
    }

    public User getUserByEmployerProfile(EmployerProfile employerProfile) {
        return employerProfileDAO.getUserByEmployerProfileDAO(employerProfile);
    }

}
