package com.jm.jobseekerplatform.dto;

import com.jm.jobseekerplatform.model.EmployerReviews;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;

public class SeekerReviewDTO {

    public EmployerReviews employerReviews;
    public SeekerProfile seekerProfile;

    public SeekerReviewDTO() {}

    public SeekerReviewDTO(EmployerReviews employerReviews, SeekerProfile seekerProfile) {
        this.employerReviews = employerReviews;
        this. seekerProfile = seekerProfile;
    }

    public EmployerReviews getEmployerReviews() {
        return employerReviews;
    }

    public SeekerProfile getSeekerProfile() {
        return seekerProfile;
    }

    public void setEmployerReviews(EmployerReviews employerReviews) {
        this.employerReviews = employerReviews;
    }

    public void setSeekerProfile(SeekerProfile seekerProfile) {
        this.seekerProfile = seekerProfile;
    }
}
