package com.jm.jobseekerplatform.service.impl.tokens;

import com.jm.jobseekerplatform.dao.impl.tokens.BaseTokenDAO;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.tokens.VerificationToken;
import com.jm.jobseekerplatform.service.impl.profiles.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("verificationTokenService")
@Transactional
public class VerificationTokenService extends BaseTokenService<VerificationToken> {

    @Autowired
    private ProfileService profileService;

    @Autowired
    public VerificationTokenService(BaseTokenDAO<VerificationToken> baseTokenDAO) {
        super(baseTokenDAO);
    }

    public void completeRegistration(VerificationToken token) {
        token.getUser().setConfirm(true);
        token.getUser().setEnabled(true);
        Profile profile = token.getUser().getProfile();
        profileService.checkedState(profile);
        abstractDAO.delete(token);
    }
}
