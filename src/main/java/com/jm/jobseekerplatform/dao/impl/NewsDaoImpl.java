package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.interfaces.NewsDao;
import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.Subscription;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public abstract class NewsDaoImpl implements NewsDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Page<News> getBySubscriptions(Iterable<Subscription> subscriptions, Pageable pageable) {
        List<News> news = new ArrayList<>();
        for (Subscription s : subscriptions) {
            Query query = entityManager.createQuery("SELECT distinct n FROM News n " +
                    "JOIN n.author na JOIN n.tags nt where na = :newsAuthor and nt in :tags ORDER BY date DESC ", News.class);
            query.setParameter("newsAuthor", s.getEmployerProfile())
                    .setParameter("tags", s.getTags());
            List<News> newsList = query.getResultList();
            news.addAll(newsList);
        }
        return getPageNews(news, pageable);
    }

    public Page<News> getBySeekerProfileTags(SeekerProfile profile, Pageable pageable) {
        List<News> news = new ArrayList<>();
        Query query = entityManager.createQuery("SELECT distinct n FROM News n " +
                "JOIN n.tags nt WHERE nt in :tags", News.class);
        query.setParameter("tags", profile.getTags());
        List<News> newsList = query.getResultList();
        news.addAll(newsList);
        return getPageNews(news, pageable);
    }

    private Page<News> getPageNews(List<News> news, Pageable pageable) {
        int start = (int) pageable.getOffset();
        if (start > news.size()) {
            return new PageImpl<>(news.subList(news.size(), news.size()));
        }
        int end = (start + pageable.getPageSize()) > news.size() ? news.size() : (start + pageable.getPageSize());
        return new PageImpl<>(news.subList(start, end), pageable, news.size());
    }
}
