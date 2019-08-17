package com.jm.jobseekerplatform.aop;

import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.service.impl.NewsService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Aspect
@Component
public class NewsCounter {

    @Autowired
    private NewsService newsService;

    @AfterReturning(
            pointcut = "execution(* com.jm.jobseekerplatform.controller.rest" +
                    ".NewsRestController.getAllNewsBySeekerProfileId(..))",
            returning = "retVal")
    public void doAccessCheck(Object retVal) {
        ResponseEntity responseEntity = (ResponseEntity) retVal;
        Collection entityBody = (Collection) responseEntity.getBody();

        entityBody.forEach(x -> {
            News news = (News) x;
            Long totalViews = news.getNumberOfViews();
            if (totalViews == null) {
                totalViews = 0L;
            }
            news.setNumberOfViews(totalViews + 1);
            newsService.update(news);
        });
    }
}
