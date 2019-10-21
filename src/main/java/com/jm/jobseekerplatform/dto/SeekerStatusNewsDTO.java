package com.jm.jobseekerplatform.dto;

import com.jm.jobseekerplatform.model.News;
import com.jm.jobseekerplatform.model.SeekerStatus;

public class SeekerStatusNewsDTO {

    private News news;
    private SeekerStatus seekerStatus;

    public SeekerStatusNewsDTO() {}

    public SeekerStatusNewsDTO(News news, SeekerStatus seekerStatus) {
        this.news         = news;
        this.seekerStatus = seekerStatus;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public SeekerStatus getSeekerStatus() {
        return seekerStatus;
    }

    public void setSeekerStatus(SeekerStatus seekerStatus) {
        this.seekerStatus = seekerStatus;
    }

}
