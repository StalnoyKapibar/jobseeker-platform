package com.jm.jobseekerplatform.config;

import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserService userService;

    public LoginFailureHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        AuthenticationException exception) throws IOException, ServletException {
        httpServletRequest.getSession().invalidate();
        if (exception.getMessage().equals("User is disabled")) {
            User user = userService.findByEmail(httpServletRequest.getParameter("j_username"));
            Date expireDate = user.getProfile().getExpiryBlock();
            LocalDateTime date = expireDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime localDate = LocalDateTime.now();
            long diff = ChronoUnit.SECONDS.between(localDate, date);
            long howManyDays = TimeUnit.SECONDS.toDays(diff);
            long howManyHours = TimeUnit.SECONDS.toHours(diff - TimeUnit.DAYS.toSeconds(howManyDays));
            long howManyMinutes = TimeUnit.SECONDS.toMinutes(diff - TimeUnit.DAYS.toSeconds(howManyDays)
                    - TimeUnit.HOURS.toSeconds(howManyHours));
            long howManySeconds = (diff - TimeUnit.DAYS.toSeconds(howManyDays) - TimeUnit.HOURS.toSeconds(howManyHours)
                    - TimeUnit.MINUTES.toSeconds(howManyMinutes));
            StringBuilder timeRemaining = new StringBuilder("Ваша учетная запись заблокирована. " +
                    "До конца блокировки осталось: ");
            timeRemaining.append(howManyDays == 0 ? "" : howManyDays +  " дней, ")
                         .append(howManyHours == 0 ? "" : howManyHours + " часов, ")
                         .append(howManyMinutes == 0 ? "" : howManyMinutes + " минут, ")
                         .append(howManySeconds == 0 ? "" : howManySeconds + " секунд.");
            httpServletRequest.getSession().setAttribute("error", timeRemaining);
            httpServletResponse.sendRedirect("/login?timeRemaining");
        } else if (exception.getMessage().equalsIgnoreCase("Bad credentials")) {
            httpServletRequest.getSession().setAttribute("error", "Неверный логин или пароль.");
            httpServletResponse.sendRedirect("/login?error");
        } else if (exception.getMessage().equalsIgnoreCase("User account has expired")) {
            httpServletRequest.getSession().setAttribute("error",
                    "Срок действия учетной записи истек.");
            httpServletResponse.sendRedirect("/login?expired");
        } else if (exception.getMessage().equalsIgnoreCase("blocked")) {
            httpServletRequest.getSession().setAttribute("error", "Ваша учетная запись заблокирована.");
            httpServletResponse.sendRedirect("/login?blocked");
        } else {
            httpServletResponse.sendRedirect("/login?unknownError");
        }
    }
}