package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.reports.Report;
import com.jm.jobseekerplatform.service.impl.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/api/report")
public class ReportRestController {
    @Autowired
    private ReportService reportService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    private ResponseEntity<Void> addReport (@RequestBody Report report){
        reportService.add(report);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
}
