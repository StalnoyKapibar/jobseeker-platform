package com.jm.jobseekerplatform.dto;

import com.jm.jobseekerplatform.model.Resume;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import org.springframework.data.domain.PageImpl;
import java.util.List;

public class ResumePageDTO extends PageImpl<Resume> {

    private int totalPages;
    private List<SeekerProfile> seeker;

    public ResumePageDTO(List<Resume> content) {
        super(content);
    }

    public ResumePageDTO(List<Resume> content, int totalPages) {
        super(content);
        this.totalPages = totalPages;
    }

    public ResumePageDTO(List<Resume> content, int totalPages,  List<SeekerProfile> seeker) {
        super(content);
        this.totalPages = totalPages;
        this.seeker = seeker;
    }

	public ResumePageDTO(List<Resume> resumeList, List<SeekerProfile> seeker) {
		super(resumeList);
		this.seeker = seeker;
	}

    @Override
    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

	public List<SeekerProfile> getSeeker() {
		return seeker;
	}

	public void setSeeker(List<SeekerProfile> seeker) {
		this.seeker = seeker;
	}

}
