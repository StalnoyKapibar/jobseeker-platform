package com.jm.jobseekerplatform.dto;

import com.jm.jobseekerplatform.model.News;

public class SeekerCountDTO {

    private News news;
    private Long viewsNumber;

    public SeekerCountDTO() {}

    public SeekerCountDTO(News news, Long viewsNumber) {
        this.news        = news;
        this.viewsNumber = viewsNumber;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public Long getViewsNumber() {
        return viewsNumber;
    }

    public void setViewsNumber(Long viewsNumber) {
        this.viewsNumber = viewsNumber;
    }

}
