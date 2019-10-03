package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.dto.NewsDTO;
import com.jm.jobseekerplatform.model.DraftNews;
import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.Subscription;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.model.comments.Comment;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.DraftNewsService;
import com.jm.jobseekerplatform.service.impl.NewsService;
import com.jm.jobseekerplatform.service.impl.TagService;
import com.jm.jobseekerplatform.service.impl.comments.CommentService;
import com.jm.jobseekerplatform.service.impl.profiles.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.profiles.ProfileService;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/news")
public class NewsRestController {

    public static final int MIN_NUMBER_OF_VIEWS_FOR_VALIDATE = 10;

    @Autowired
    private DraftNewsService draftNewsService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private EmployerProfileService employerProfileService;

    @Autowired
    private SeekerProfileService seekerProfileService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CommentService commentService;

    @RolesAllowed({"ROLE_EMPLOYER"})
    @PostMapping("/add")
    public ResponseEntity addNews(@RequestBody News news,
                                  @RequestParam("tags") Set<String> tags,
                                  Authentication authentication) {
        news.setAuthor(employerProfileService.getById(((User) authentication.getPrincipal()).getProfile().getId()));
        Set<Tag> tagSet = tagService.getTagsByStringNames(tags);
        news.setTags(tagSet);
        newsService.add(news);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RolesAllowed({"ROLE_EMPLOYER"})
    @GetMapping ("/delete/{newsId}")
    public ResponseEntity deleteNews(@PathVariable("newsId") Long newsId) {
        newsService.deleteById(newsId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RolesAllowed({"ROLE_EMPLOYER"})
    @GetMapping("/{newsId}")
    public ResponseEntity<NewsDTO> getNewsById(@PathVariable("newsId") Long newsId) {
        NewsDTO newsDTO = new NewsDTO();
        News news = newsService.getById(newsId);
        newsDTO.setNews(news);
        newsDTO.setOnValidation(draftNewsService.isNewsOnValidation(newsId));
        newsDTO.setNeedValidate(isNewsNeedValidate(news));
        return new ResponseEntity<>(newsDTO, HttpStatus.OK);
    }

    @RolesAllowed ({"ROLE_EMPLOYER"})
    @GetMapping ("/")
    public ResponseEntity<List<News>> getAllNewsByEmployerProfileId(@RequestParam("newsPageCount") int newsPageCount,
                                                                    Authentication authentication) {
        Sort sort = new Sort(Sort.Direction.DESC, "date");
        Long employerProfileId = ((User) authentication.getPrincipal()).getProfile().getId();
        List<News> news = newsService.getAllByEmployerProfileId(employerProfileService.getById(employerProfileId),
                PageRequest.of(newsPageCount, 10, sort)).getContent();
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    @GetMapping ("/all_seeker_news")
    public ResponseEntity<List<News>> getAllNewsBySeekerProfileId(@RequestParam("seekerProfileId") Long seekerProfileId,
                                                                  @RequestParam("newsPageCount") int newsPageCount) {
        Set<Subscription> subscriptions = seekerProfileService.getById(seekerProfileId).getSubscriptions();
        if (subscriptions.size() == 0) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        Sort sort = new Sort(Sort.Direction.DESC, "date");
        List<News> news = newsService.getAllBySubscription(subscriptions,
                PageRequest.of(newsPageCount, 10, sort)).getContent();
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    @PreAuthorize("principal.profile.id.equals(@newsService.getById(#newsId).author.id)")
    @PostMapping ("/editNews")
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
