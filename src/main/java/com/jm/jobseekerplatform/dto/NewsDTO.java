package com.jm.jobseekerplatform.dto;

import com.jm.jobseekerplatform.model.News;

public class NewsDTO {
    private News news;
    private boolean needValidate;
    private boolean onValidation;

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public boolean isNeedValidate() {
        return needValidate;
    }

    public void setNeedValidate(boolean needValidate) {
        this.needValidate = needValidate;
    }

    public boolean isOnValidation() {
        return onValidation;
    }

    public void setOnValidation(boolean onValidation) {
        this.onValidation = onValidation;
    }
}
