package com.jm.jobseekerplatform.service.impl.tokens;

import com.jm.jobseekerplatform.model.tokens.VerificationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("verificationTokenService")
@Transactional
public class VerificationTokenService extends BaseTokenService<VerificationToken> {

    public void completeRegistration(VerificationToken token) {
        token.getUser().setConfirm(true);
        abstractDAO.delete(token);
    }
}
