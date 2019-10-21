package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.SeekerStatusNewsDAOI;
import com.jm.jobseekerplatform.dao.impl.SeekerStatusNewsDAO;
import com.jm.jobseekerplatform.dto.SeekerStatusNewsDTO;
import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.SeekerStatus;
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

    public void updateAllSeekerCount(List<SeekerStatusNews> list) {
        seekerStatusNewsDAOI.saveAll(list);
    }

    public List<SeekerStatusNews> getAllSeekerCount(SeekerProfile seekerProfile) {
        return seekerStatusNewsDAO.getAllSeekerCountDAO(seekerProfile);
    }

    public SeekerStatusNews getSeekerCount(News news) {
        return seekerStatusNewsDAO.getSeekerCountDAO(news);
    }

    public void update(SeekerStatusNews seekerStatusNews) {
        seekerStatusNewsDAO.update(seekerStatusNews);
    }

    // Добавление статуса просмотренным новостям
    public List<SeekerStatusNewsDTO> addViewedNews(List<News> news,
                                                   SeekerProfile seekerProfile,
                                                   List<SeekerStatusNews> dbList) {
        List<SeekerStatusNews> scnvList = new ArrayList<>();
        List<SeekerStatusNewsDTO> scDto = new ArrayList<>();
        if (news.size() != 0) {
            for (News n : news) {
                if (dbList.size() != 0) {
                    for (int i = 0; i < dbList.size(); i++) {
                        Long dbNewsId = dbList.get(i).getNews().getId();
                        SeekerStatus dbView = dbList.get(i).getSeekerStatus();
                        Long newsId = n.getId();
                        Long id = dbList.get(i).getId();
                        if (newsId == dbNewsId && dbView.equals(SeekerStatus.NEW)) {
                            if (checkListForMatches(newsId, scnvList)) {
                                scnvList.add(addInSeekerCount(id, seekerProfile, n, SeekerStatus.VIEWED));
                                scDto.add(addInSeekerCountDTO(SeekerStatus.VIEWED, n));
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
                                scnvList.add(addInSeekerCount(null, seekerProfile, n, SeekerStatus.NEW));
                                scDto.add(addInSeekerCountDTO(SeekerStatus.NEW, n));
                            }
                        }
                    }
                } else {
                    scnvList.add(addInSeekerCount(null, seekerProfile, n, SeekerStatus.NEW));
                    scDto.add(addInSeekerCountDTO(SeekerStatus.NEW, n));
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
    private SeekerStatusNews addInSeekerCount(Long id, SeekerProfile seekerProfile, News news, SeekerStatus seekerStatus) {
        SeekerStatusNews scnv = new SeekerStatusNews();
        scnv.setId(id);
        scnv.setDate(LocalDateTime.now());
        scnv.setSeeker(seekerProfile);
        scnv.setNews(news);
        scnv.setSeekerStatus(seekerStatus);
        return scnv;
    }

    public SeekerStatusNewsDTO addInSeekerCountDTO(SeekerStatus seekerStatus, News news) {
        SeekerStatusNewsDTO seekerStatusNewsDTO = new SeekerStatusNewsDTO();
        seekerStatusNewsDTO.setNews(news);
        seekerStatusNewsDTO.setSeekerStatus(seekerStatus);
        return seekerStatusNewsDTO;
    }

    public void changeStatusViewedNews(News news) {
        SeekerStatusNews scnv = getSeekerCount(news);
        if (!scnv.getSeekerStatus().equals("READ")) {
            scnv.setSeekerStatus(SeekerStatus.READ);
            update(scnv);
        }
    }

}
