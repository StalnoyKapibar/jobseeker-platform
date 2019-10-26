package com.jm.jobseekerplatform.dto;

public class UserTimerDTO {
    private Long userId;
    private String time;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
