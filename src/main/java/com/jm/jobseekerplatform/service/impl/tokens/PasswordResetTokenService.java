package com.jm.jobseekerplatform.service.impl.tokens;


import com.jm.jobseekerplatform.model.tokens.PasswordResetToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("passwordResetTokenService")
@Transactional
public class PasswordResetTokenService extends BaseTokenService<PasswordResetToken> {

}
