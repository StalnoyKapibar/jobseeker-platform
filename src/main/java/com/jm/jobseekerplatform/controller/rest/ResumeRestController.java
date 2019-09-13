package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.Point;
import com.jm.jobseekerplatform.model.Resume;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.service.impl.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
@RequestMapping("api/resumes")
public class ResumeRestController {

	@Autowired
	private ResumeService resumeService;

	@RequestMapping("/getbyid/{resumeId}")
	public Resume getResumeById(@PathVariable Long resumeId) {
		return resumeService.getById(resumeId);
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<Page<Resume>> getSearchVacancies(@RequestBody Set<Tag> searchParam,
													@RequestParam("pageCount") int pageCount) {
		if (searchParam.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		Page<Resume> page = resumeService.findAllByTags(searchParam, PageRequest.of(pageCount, 10));
		return new ResponseEntity<>(page, HttpStatus.OK);
	}

	@RequestMapping(value = "/city/page/{page}", method = RequestMethod.POST)
	public Page<Resume> getPageOfResumes(@RequestBody Point point,
										 @RequestParam("city") String city,
										 @PathVariable("page") int page) {
		int limit = 10;
		if (city.equals("undefined")) {
			Page<Resume> pageRezume = resumeService.getAllResumes(limit, page);
			return pageRezume;
		} else {
			return resumeService.findResumesByPoint(city, point, limit, page);
		}
	}

}
