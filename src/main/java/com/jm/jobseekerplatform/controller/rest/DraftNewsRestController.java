package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.DraftNews;
import com.jm.jobseekerplatform.service.impl.DraftNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/draftNews")
@RolesAllowed({"ROLE_ADMIN"})
public class DraftNewsRestController {

    @Autowired
    DraftNewsService draftNewsService;

    @GetMapping("/")
    public ResponseEntity<List<DraftNews>> getAllDraftNews(@RequestParam(value = "filter",
            defaultValue = "ACTIVE") String filter) {
        List<DraftNews> listDraftNews;
        if (filter.equals("ALL")) {
            listDraftNews = draftNewsService.getAll();
        } else {
            listDraftNews = draftNewsService.findAllActive();
        }
        return new ResponseEntity<>(listDraftNews, HttpStatus.OK);
    }

    @GetMapping("/{draftId}")
    public ResponseEntity<DraftNews> getAllDraftNews(@PathVariable Long draftId) {
        return new ResponseEntity<>(draftNewsService.getById(draftId), HttpStatus.OK);
    }

    @GetMapping("/{draftId}/approve")
    public ResponseEntity approveDraftNews(@PathVariable Long draftId) {
        draftNewsService.approveDraft(draftId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{draftId}/reject")
    public ResponseEntity rejectDraftNews(@PathVariable Long draftId) {
        draftNewsService.rejectDraft(draftId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
