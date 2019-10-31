package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.interfaces.DraftNewsDao;
import com.jm.jobseekerplatform.model.DraftNews;
import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DraftNewsService extends AbstractService<DraftNews> {
    @Autowired
    private DraftNewsDao draftNewsDao;

    @Autowired
    private NewsService newsService;

    public boolean isNewsOnValidation(long newsId) {
        return draftNewsDao.getOnValidation(newsId) > 0;
    }

    public List<DraftNews> findAllActive() {
        return draftNewsDao.findAllByIsValidIsNullOrderByIdDesc();
    }

    public void approveDraft(long draftId) {
        DraftNews draft = draftNewsDao.getOne(draftId);
        draft.setValid(true);
        draftNewsDao.save(draft);

        News news = draft.getOriginal();
        news.setHeadline(draft.getHeadline());
        news.setDescription(draft.getDescription());
        newsService.update(news);
    }

    public void rejectDraft(long draftId) {
        DraftNews draft = draftNewsDao.getOne(draftId);
        draft.setValid(false);
        draftNewsDao.save(draft);
    }
}
