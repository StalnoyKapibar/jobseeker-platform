package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.SeekerStatusNewsDAOI;
import com.jm.jobseekerplatform.dao.impl.SeekerStatusNewsDAO;
import com.jm.jobseekerplatform.dto.SeekerStatusNewsDTO;
import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.NewsStatus;
import com.jm.jobseekerplatform.model.SeekerStatusNews;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SeekerStatusNewsService extends AbstractService<SeekerStatusNews> {

    @Autowired
    SeekerStatusNewsDAO seekerStatusNewsDAO;

    @Autowired
    SeekerStatusNewsDAOI seekerStatusNewsDAOI;

    @Autowired
    private NewsService newsService;

    public void updateAllSeekerCount(List<SeekerStatusNews> list) {
        seekerStatusNewsDAOI.saveAll(list);
    }

    public List<SeekerStatusNews> getAllSeekerStatusNews(SeekerProfile seekerProfile) {
        return seekerStatusNewsDAO.getAllSeekerStatusNewsDAO(seekerProfile);
    }

    public SeekerStatusNews getSeekerStatusNews(News news) {
        return seekerStatusNewsDAO.getSeekerStatusNewsDAO(news);
    }

    public void update(SeekerStatusNews seekerStatusNews) {
        seekerStatusNewsDAO.update(seekerStatusNews);
    }

    // Добавление статуса просмотренным новостям
    public List<SeekerStatusNewsDTO> addViewedNews(List<News> news,
                                                   SeekerProfile seekerProfile,
                                                   List<SeekerStatusNews> dbList) {
        news.forEach(x -> {
            Long totalViews = x.getNumberOfViews();
            if (totalViews == null) totalViews = 0L;
            x.setNumberOfViews(totalViews + 1);
            newsService.update(x);
        });

        List<SeekerStatusNews> scnvList = new ArrayList<>();
        List<SeekerStatusNewsDTO> scDto = new ArrayList<>();
        if (news.size() != 0) {
            for (News n : news) {
                if (dbList.size() != 0) {
                    for (int i = 0; i < dbList.size(); i++) {
                        Long dbNewsId = dbList.get(i).getNews().getId();
                        NewsStatus dbView = dbList.get(i).getNewsStatus();
                        Long newsId = n.getId();
                        Long id = dbList.get(i).getId();
                        if (newsId == dbNewsId && dbView.equals(NewsStatus.NEW)) {
                            if (checkListForMatches(newsId, scnvList)) {
                                scnvList.add(addInSeekerCount(id, seekerProfile, n, NewsStatus.VIEWED));
                                scDto.add(addInSeekerCountDTO(NewsStatus.VIEWED, n));
                            }
                        } else if (newsId == dbNewsId) {
                            scDto.add(addInSeekerCountDTO(dbView, n));
                            break;
                        } else {
                            boolean bool = true;
                            for (int j = 0; j < dbList.size(); j++) {
                                if (newsId == dbList.get(j).getNews().getId()) {
                                    bool = false;
                                    break;
                                }
                            }
                            if (bool && checkListForMatches(newsId, scnvList)) {
                                scnvList.add(addInSeekerCount(null, seekerProfile, n, NewsStatus.NEW));
                                scDto.add(addInSeekerCountDTO(NewsStatus.NEW, n));
                            }
                        }
                    }
                } else {
                    scnvList.add(addInSeekerCount(null, seekerProfile, n, NewsStatus.NEW));
                    scDto.add(addInSeekerCountDTO(NewsStatus.NEW, n));
                }
            }
        }
        if (scnvList.size() > 0) {
            updateAllSeekerCount(scnvList);
        }
        return scDto;
    }

    // Проверка есть/нет такого-же id в создаваемой коллекции
    private boolean checkListForMatches(Long newsId, List<SeekerStatusNews> scnvList) {
        for (int i = 0; i < scnvList.size(); i++) {
            if (newsId == scnvList.get(i).getNews().getId()) return false;
        }
        return true;
    }

    // Добавление в создаваемую коллекцию
    private SeekerStatusNews addInSeekerCount(Long id, SeekerProfile seekerProfile, News news, NewsStatus newsStatus) {
        SeekerStatusNews scnv = new SeekerStatusNews();
        scnv.setId(id);
        scnv.setDate(LocalDateTime.now());
        scnv.setSeeker(seekerProfile);
        scnv.setNews(news);
        scnv.setNewsStatus(newsStatus);
        return scnv;
    }

    public SeekerStatusNewsDTO addInSeekerCountDTO(NewsStatus newsStatus, News news) {
        SeekerStatusNewsDTO seekerStatusNewsDTO = new SeekerStatusNewsDTO();
        seekerStatusNewsDTO.setNews(news);
        seekerStatusNewsDTO.setNewsStatus(newsStatus);
        return seekerStatusNewsDTO;
    }

    public void changeStatusViewedNews(News news) {
        SeekerStatusNews scnv = getSeekerStatusNews(news);
        if (!scnv.getNewsStatus().equals("READ")) {
            scnv.setNewsStatus(NewsStatus.READ);
            update(scnv);
        }
    }

}
