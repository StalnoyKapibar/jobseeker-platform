package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.profiles.ProfileService;
import com.jm.jobseekerplatform.service.impl.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Service("userDetailsService")
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userService.findByEmail(s);
        if (user == null) {
            throw new UsernameNotFoundException("USER not found");
        } else if (!user.isEnabled() && user.getProfile().getExpiryBlock() != null) {
            long diff = ChronoUnit.SECONDS.between(LocalDateTime.now(), user.getProfile().getExpiryBlock().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime());
            if (diff <= 0) {
                user.setEnabled(true);
                userService.update(user);
                profileService.unblock(user.getProfile());
            }
        }
        return user;
    }
}
