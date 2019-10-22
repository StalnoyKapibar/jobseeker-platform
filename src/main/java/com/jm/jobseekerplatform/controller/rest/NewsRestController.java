package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.dto.NewsDTO;
import com.jm.jobseekerplatform.dto.SeekerStatusNewsDTO;
import com.jm.jobseekerplatform.model.*;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.DraftNewsService;
import com.jm.jobseekerplatform.service.impl.NewsService;
import com.jm.jobseekerplatform.service.impl.SeekerStatusNewsService;
import com.jm.jobseekerplatform.service.impl.TagService;
import com.jm.jobseekerplatform.service.impl.profiles.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    SeekerStatusNewsService seekerStatusNewsService;

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
    public ResponseEntity<List<SeekerStatusNewsDTO>> getAllNewsBySeekerProfileId(
                                                    @RequestParam("seekerProfileId") Long seekerProfileId,
                                                    @RequestParam("newsPageCount") int newsPageCount) {
        SeekerProfile profile = seekerProfileService.getById(seekerProfileId);
        Set<Subscription> subscriptions = profile.getSubscriptions();
        if (subscriptions.size() == 0) {
            Sort sort = new Sort(Sort.Direction.DESC, "date");
            List<News> tagNews = newsService.getAllBySeekerProfileTags(profile, PageRequest.of(newsPageCount, 10, sort))
                    .getContent();
            List<SeekerStatusNewsDTO> scDto = new ArrayList<>();
            for (News n : tagNews) {
                scDto.add(seekerStatusNewsService.addInSeekerCountDTO(SeekerStatus.VIEWED, n));
            }
            return new ResponseEntity<>(scDto, HttpStatus.OK);
        }
        Sort sort = new Sort(Sort.Direction.DESC, "date");
        List<News> tagNews = newsService.getAllBySeekerProfileTags(profile, PageRequest.of(newsPageCount, 10, sort))
                .getContent();
        List<News> subscriptionNews = newsService.getAllBySubscriptions(subscriptions,
                PageRequest.of(newsPageCount, 10, sort)).getContent();
        List<News> news = new ArrayList<>(subscriptionNews);
        news.addAll(tagNews);

        news.forEach(x -> {
            Long totalViews = x.getNumberOfViews();
            if (totalViews == null) totalViews = 0L;
            x.setNumberOfViews(totalViews + 1);
            newsService.update(x);
        });

        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        List<SeekerStatusNews> dbList = seekerStatusNewsService.getAllSeekerCount(seekerProfile);
        List<SeekerStatusNewsDTO> scDto = seekerStatusNewsService.addViewedNews(news, seekerProfile, dbList);
        return new ResponseEntity<>(scDto, HttpStatus.OK);
    }

    @PreAuthorize("principal.profile.id.equals(@newsService.getById(#newsId).author.id)")
    @PostMapping ("/editNews")
    public ResponseEntity editNews(@RequestParam("newsId") Long newsId,
                                   @RequestParam("newsHeadline") String newsHeadline,
                                   @RequestParam("newsDescription") String newsDescription) {
        News news = newsService.getById(newsId);
        //Если обновление новости требует последующей проверки администратором
        if( isNewsNeedValidate(news) ) {
            //Только одно обновление новости для проверки допустимо
            if( draftNewsService.isNewsOnValidation(newsId) ) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
            DraftNews draftNews = new DraftNews();
            draftNews.setHeadline(newsHeadline);
            draftNews.setDescription(newsDescription);
            draftNews.setOriginal(news);
            draftNewsService.add(draftNews);
        } else {
            news.setHeadline(newsHeadline);
            news.setDescription(newsDescription);
            newsService.update(news);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    private boolean isNewsNeedValidate(News news) {
        return news.getNumberOfViews() != null ?
                news.getNumberOfViews() >= MIN_NUMBER_OF_VIEWS_FOR_VALIDATE : false;
    }
}
