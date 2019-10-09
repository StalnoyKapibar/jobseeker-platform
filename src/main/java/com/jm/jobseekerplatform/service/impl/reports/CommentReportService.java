package com.jm.jobseekerplatform.service.impl.reports;

import com.jm.jobseekerplatform.model.reports.CommentReport;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("commentReportService")
@Transactional
public class CommentReportService extends AbstractService<CommentReport> {
}
