package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.users.SeekerUser;
import com.jm.jobseekerplatform.service.impl.users.SeekerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/admin")
public class AdminRestController {
    @Autowired
    private SeekerUserService seekerUserService;

    @RequestMapping(value = "/seekers/all", method = RequestMethod.GET)
    public ResponseEntity<Integer> getSumOfAllSeekers(){
        List<SeekerUser> seekerUserList = seekerUserService.getAll();
        Integer sum = seekerUserList.size();
        return new ResponseEntity<>(sum, HttpStatus.OK);
    }
}
