package com.jm.jobseekerplatform.dto;

import com.jm.jobseekerplatform.model.Vacancy;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public class VacancyPageDTO extends PageImpl<Vacancy> {

    int totalPages;

    public VacancyPageDTO(List<Vacancy> content) {
        super(content);
    }

    public VacancyPageDTO(List<Vacancy> content, int totalPages) {
        super(content);
        this.totalPages = totalPages;
    }

    @Override
    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
