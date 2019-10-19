package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.Subscription;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository("newsDAO")
public class NewsDAO extends AbstractDAO<News> {

    @Override
    public void deleteById(Long newsId) {
        entityManager.createQuery("delete from News where id =: id")
                .setParameter("id", newsId).executeUpdate();
    }

    public Page<News> getBySubscriptions(Iterable<Subscription> subscriptions, Pageable pageable) {
        List<News> news = new ArrayList<>();

        for (Subscription s : subscriptions) {
            Query query = entityManager.createQuery("SELECT distinct n FROM News n " +
                    "JOIN n.author na JOIN n.tags nt where na = :newsAuthor and nt in :tags", News.class);
            query.setParameter("newsAuthor", s.getEmployerProfile())
                    .setParameter("tags", s.getTags());

            List<News> newsList = query.getResultList();
            news.addAll(newsList);
        }

        int start = (int) pageable.getOffset();
        if (start > news.size()) {
            return new PageImpl<>(news.subList(news.size(), news.size()));
        }
        int end = (start + pageable.getPageSize()) > news.size() ? news.size() : (start + pageable.getPageSize());

        return new PageImpl<>(news.subList(start, end), pageable, news.size());
    }

    public Page<News> getBySeekerProfileTags(SeekerProfile profile, Pageable pageable) {
        Set<News> tagNewsSet = new HashSet<>();
        Query query = entityManager.createQuery("SELECT distinct n FROM News n " +
                "JOIN n.tags nt WHERE nt in :tags", News.class);
        query.setParameter("tags", profile.getTags());
        List tagNews = query.getResultList();
        tagNewsSet.addAll(tagNews);

        List<News> news = new ArrayList<>(tagNewsSet);

        int start = (int) pageable.getOffset();
        if (start > news.size()) {
            return new PageImpl<>(news.subList(news.size(), news.size()));
        }
        int end = (start + pageable.getPageSize()) > news.size() ? news.size() : (start + pageable.getPageSize());

        return new PageImpl<>(news.subList(start, end), pageable, news.size());
    }

}
