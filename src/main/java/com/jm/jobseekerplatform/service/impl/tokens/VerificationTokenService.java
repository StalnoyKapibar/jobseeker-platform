package com.jm.jobseekerplatform.service.impl.tokens;

import com.jm.jobseekerplatform.dao.impl.tokens.VerificationTokenDAO;
import com.jm.jobseekerplatform.model.tokens.VerificationToken;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("verificationTokenService")
@Transactional
public class VerificationTokenService extends BaseTokenService<VerificationToken> {

    @Autowired
    private VerificationTokenDAO dao;

    public void completeRegistration(VerificationToken token) {
        token.getUser().setConfirm(true);
        dao.delete(token);
    }
}
