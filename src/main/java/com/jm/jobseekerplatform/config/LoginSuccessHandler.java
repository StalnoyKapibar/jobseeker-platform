package com.jm.jobseekerplatform.config;

import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    public LoginSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        User principal = (User) authentication.getPrincipal();
        User user = userService.getById(principal.getId());
        user.setDate(LocalDateTime.now());
        userService.update(user);
        if (authentication.isAuthenticated()) {
            httpServletResponse.sendRedirect("/");
        } else {
            throw new IllegalStateException();
        }
    }
}
