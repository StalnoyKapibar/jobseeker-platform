package com.jm.jobseekerplatform.dto;

import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.NewsStatus;

public class SeekerStatusNewsDTO {

    private News news;
    private NewsStatus newsStatus;

    public SeekerStatusNewsDTO() {}

    public SeekerStatusNewsDTO(News news, NewsStatus newsStatus) {
        this.news = news;
        this.newsStatus = newsStatus;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public NewsStatus getNewsStatus() {
        return newsStatus;
    }

    public void setNewsStatus(NewsStatus newsStatus) {
        this.newsStatus = newsStatus;
    }

}
