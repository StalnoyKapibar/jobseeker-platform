package com.jm.jobseekerplatform.dto;

public class EmployerTimerDTO {
    private Long userId;
    private Long time;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
