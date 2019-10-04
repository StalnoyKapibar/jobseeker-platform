package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.model.reports.Report;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("reportService")
@Transactional
public class ReportService extends AbstractService<Report> {

}
