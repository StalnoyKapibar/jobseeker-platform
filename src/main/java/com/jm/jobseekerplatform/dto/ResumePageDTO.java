package com.jm.jobseekerplatform.dto;

import com.jm.jobseekerplatform.model.Resume;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class ResumePageDTO extends PageImpl<Resume> {
    private int totalPages;

    public ResumePageDTO(List<Resume> content) {
        super(content);
        this.totalPages=content.size();
    }

    @Override
    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
