package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.Meeting;
import com.jm.jobseekerplatform.model.Status;
import com.jm.jobseekerplatform.service.impl.MeetingService;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/meetings")
public class MeetingRestController {

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private SeekerProfileService seekerProfileService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void saveMeeting(@RequestParam("vacancyId") Long vacancyId, @RequestParam("seekerId") Long seekerId) {
        Meeting meeting = new Meeting();
        meeting.setVacancy(vacancyService.getById(vacancyId));
        meeting.setSeekerProfile(seekerProfileService.getById(seekerId));
        meeting.setStatus(Status.NOT_CONFIRMED);
        meetingService.addMeeting(meeting);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateMeeting(@PathVariable("id") Long id, @RequestBody Meeting meeting) {
        Meeting toUpdate = meetingService.getMeetingById(id);
        toUpdate.setDate(meeting.getDate());
        toUpdate.setStatus(meeting.getStatus());
        meetingService.updateMeeting(toUpdate);
    }

    @GetMapping("/{id}")
    public Meeting getMeeting(@PathVariable("id") Long id) {
        return meetingService.getMeetingById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void patchMeeting(@PathVariable("id") Long id, @RequestBody Status status) {
        Meeting toUpdate = meetingService.getMeetingById(id);
        toUpdate.setStatus(status);
        meetingService.updateMeeting(toUpdate);
    }
}
