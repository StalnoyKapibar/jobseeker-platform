package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.interfaces.SeekerStatusNewsDao;
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
    SeekerStatusNewsDao seekerStatusNewsDao;

    @Autowired
    private NewsService newsService;

    public void updateAllSeekerCount(List<SeekerStatusNews> list) {
        seekerStatusNewsDao.saveAll(list);
    }

    public List<SeekerStatusNews> getAllSeekerStatusNews(SeekerProfile seekerProfile) {
        return seekerStatusNewsDao.getAllSeekerStatusNewsDAO(seekerProfile);
    }

    public SeekerStatusNews getSeekerStatusNews(News news, SeekerProfile seekerProfile) {
        return seekerStatusNewsDao.getSeekerStatusNewsDAO(news, seekerProfile);
    }

    public void update(SeekerStatusNews seekerStatusNews) {
        seekerStatusNewsDao.save(seekerStatusNews);
    }

    // Добавление статуса просмотренным новостям
    public List<SeekerStatusNewsDTO> addViewedNews(List<News> news,
                                                   SeekerProfile seekerProfile,
                                                   List<SeekerStatusNews> dbList) {
        countNumberOfViews(news);

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
                                scnvList.add(addInSeekerStatusNews(id, seekerProfile, n, NewsStatus.VIEWED));
                                scDto.add(addInSeekerStatusNewsDTO(NewsStatus.VIEWED, n));
                            }
                        } else if (newsId == dbNewsId) {
                            scDto.add(addInSeekerStatusNewsDTO(dbView, n));
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
                                scnvList.add(addInSeekerStatusNews(null, seekerProfile, n, NewsStatus.NEW));
                                scDto.add(addInSeekerStatusNewsDTO(NewsStatus.NEW, n));
                            }
                        }
                    }
                } else {
                    scnvList.add(addInSeekerStatusNews(null, seekerProfile, n, NewsStatus.NEW));
                    scDto.add(addInSeekerStatusNewsDTO(NewsStatus.NEW, n));
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
    private SeekerStatusNews addInSeekerStatusNews(Long id, SeekerProfile seekerProfile, News news, NewsStatus newsStatus) {
        SeekerStatusNews ssn = new SeekerStatusNews();
        ssn.setId(id);
        ssn.setDate(LocalDateTime.now());
        ssn.setSeeker(seekerProfile);
        ssn.setNews(news);
        ssn.setNewsStatus(newsStatus);
        return ssn;
    }

    public void countNumberOfViews(List<News> news) {
        news.forEach(x -> {
            Long totalViews = x.getNumberOfViews();
            if (totalViews == null) {
                totalViews = 0L;
            }
            x.setNumberOfViews(totalViews + 1);
            newsService.update(x);
        });
    }

    public SeekerStatusNewsDTO addInSeekerStatusNewsDTO(NewsStatus newsStatus, News news) {
        SeekerStatusNewsDTO seekerStatusNewsDTO = new SeekerStatusNewsDTO();
        seekerStatusNewsDTO.setNews(news);
        seekerStatusNewsDTO.setNewsStatus(newsStatus);
        return seekerStatusNewsDTO;
    }

    public void changeNewsStatus(News news, SeekerProfile seekerProfile) {
        SeekerStatusNews ssn = getSeekerStatusNews(news, seekerProfile);
        if (ssn.getId() == null) {
            ssn.setNews(news);
            ssn.setDate(LocalDateTime.now());
            ssn.setNewsStatus(NewsStatus.READ);
            ssn.setSeeker(seekerProfile);
            update(ssn);
        } else if (ssn.getId() != null && !ssn.getNewsStatus().equals(NewsStatus.READ)) {
            ssn.setNewsStatus(NewsStatus.READ);
            update(ssn);
        }
    }

}
