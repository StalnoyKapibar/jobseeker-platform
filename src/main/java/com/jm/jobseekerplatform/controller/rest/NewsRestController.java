package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.service.impl.NewsService;
import com.jm.jobseekerplatform.service.impl.profiles.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/news")
public class NewsRestController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private EmployerProfileService employerProfileService;

    @Autowired
    private SeekerProfileService seekerProfileService;

    @RolesAllowed({"ROLE_EMPLOYER"})
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity addNews(@RequestBody News news,
                           @RequestParam("employerProfileId") Long employerProfileId) {
        news.setAuthor(employerProfileService.getById(employerProfileId));
        newsService.add(news);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{newsId}", method = RequestMethod.GET)
    public ResponseEntity deleteNews(@PathVariable("newsId") Long newsId) {
        newsService.deleteById(newsId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/{newsId}", method = RequestMethod.GET)
    public ResponseEntity<News> getNewsById(@PathVariable("newsId") Long newsId) {
        return new ResponseEntity<>(newsService.getById(newsId), HttpStatus.OK);
    }

    @RequestMapping(value = "/all_employer_news", method = RequestMethod.GET)
    public ResponseEntity<List<News>> getAllNewsByEmployerProfileId(@RequestParam("employerProfileId") Long employerProfileId,
                                                                    @RequestParam("newsPageCount") int newsPageCount) {
        Sort sort = new Sort(Sort.Direction.DESC, "date");
        List<News> news = newsService.getAllByEmployerProfileId(employerProfileService.getById(employerProfileId),
                PageRequest.of(newsPageCount, 10, sort)).getContent();
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    @RequestMapping(value = "/all_seeker_news", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<List<News>> getAllNewsBySeekerProfileId(@RequestParam("seekerProfileId") Long seekerProfileId,
                                                           @RequestParam("newsPageCount") int newsPageCount) {
        Set<EmployerProfile> employerProfiles = seekerProfileService.getById(seekerProfileId).getSubscriptions();
        if (employerProfiles.size() == 0) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        Sort sort = new Sort(Sort.Direction.DESC, "date");
        List<News> news = newsService.getAllBySeekerProfileId(employerProfiles,
                PageRequest.of(newsPageCount, 10, sort)).getContent();
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    @RequestMapping(value = "/editNews", method = RequestMethod.POST)
    public ResponseEntity editNews(@RequestParam("newsId") Long newsId,
                                   @RequestParam("newsHeadline") String newsHeadline,
                                   @RequestParam("newsDescription") String newsDescription) {
        News news = newsService.getById(newsId);
        news.setHeadline(newsHeadline);
        news.setDescription(newsDescription);
        newsService.update(news);
        return new ResponseEntity(HttpStatus.OK);
    }
}
