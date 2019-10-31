package com.jm.jobseekerplatform.service.impl.users;

import com.jm.jobseekerplatform.dao.interfaces.profiles.EmployerProfileDao;
import com.jm.jobseekerplatform.dao.interfaces.users.EmployerUserDao;
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
    private EmployerUserDao employerUserDao;

	@Autowired
	private EmployerProfileDao employerProfileDao;

    public Page<EmployerUser> findAll(Pageable pageable) {
        return employerUserDao.findAll(pageable);
    }

    public EmployerUser getByProfileId(Long employerProfileId) {
        return employerUserDao.findByProfileId(employerProfileId);
    }

	public EmployerProfile getEmployerProfileByVacancyID(long id) {
		return employerProfileDao.findByVacancyId(id);
	}

	public List<EmployerUser> getEmployerUsersByDatePeriod(LocalDateTime startDate, LocalDateTime endDate){
        return employerUserDao.getEmployerUsersByDatePeriod(startDate, endDate);
    }

    public User getUserByEmployerProfile(EmployerProfile employerProfile) {
        return employerProfileDao.findByEmployerProfile(employerProfile);
    }
}
