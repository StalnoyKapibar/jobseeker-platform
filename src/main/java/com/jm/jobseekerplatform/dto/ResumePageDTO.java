package com.jm.jobseekerplatform.dto;

import com.jm.jobseekerplatform.model.Resume;
import com.jm.jobseekerplatform.model.SeekerTest;
import org.springframework.data.domain.PageImpl;
import java.util.List;

public class ResumePageDTO extends PageImpl<Resume> {

    private int totalPages;
    private List<SeekerTest> seeker;

    public ResumePageDTO(List<Resume> content) {
        super(content);
    }

    public ResumePageDTO(List<Resume> content, int totalPages) {
        super(content);
        this.totalPages = totalPages;
    }

    public ResumePageDTO(List<Resume> content, int totalPages, List<SeekerTest> seeker) {
        super(content);
        this.totalPages = totalPages;
        this.seeker = seeker;
    }

    @Override
    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<SeekerTest> getSeeker() {
        return seeker;
    }

    public void setSeeker(List<SeekerTest> seeker) {
        this.seeker = seeker;
    }
}








//package com.jm.jobseekerplatform.dto;
//
//import com.jm.jobseekerplatform.model.Resume;
//import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
//import org.springframework.data.domain.PageImpl;
//import java.util.List;
//
//public class ResumePageDTO extends PageImpl<Resume> {
//
//    private int totalPages;
//    private List<SeekerProfile> seekerProfile;
//
//    public ResumePageDTO(List<Resume> content) {
//        super(content);
//    }
//
//    public ResumePageDTO(List<Resume> content, int totalPages) {
//        super(content);
//        this.totalPages = totalPages;
//    }
//
//    public ResumePageDTO(List<Resume> content, int totalPages, List<SeekerProfile> seekerProfile) {
//        super(content);
//        this.totalPages = totalPages;
//        this.seekerProfile = seekerProfile;
//    }
//
//    @Override
//    public int getTotalPages() {
//        return totalPages;
//    }
//
//    public void setTotalPages(int totalPages) {
//        this.totalPages = totalPages;
//    }
//
//    public List<SeekerProfile> getSeekerProfiles() {
//        return seekerProfile;
//    }
//
//    public void setSeekerProfiles(List<SeekerProfile> seekerProfiles) {
//        this.seekerProfile = seekerProfiles;
//    }
//
//}