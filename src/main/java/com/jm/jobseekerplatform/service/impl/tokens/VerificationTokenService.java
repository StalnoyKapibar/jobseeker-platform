package com.jm.jobseekerplatform.service.impl.tokens;

import com.jm.jobseekerplatform.dao.impl.tokens.VerificationTokenDAO;
import com.jm.jobseekerplatform.model.tokens.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("verificationTokenService")
@Transactional
public class VerificationTokenService extends BaseTokenService<VerificationToken> {

    @Autowired
    public void setVerificationTokenDAO(VerificationTokenDAO verificationTokenDAO) {
        super.baseTokenDAO = verificationTokenDAO;
    }

    public void completeRegistration(VerificationToken token) {
        token.getUser().setConfirm(true);
        baseTokenDAO.delete(token);
    }

}
