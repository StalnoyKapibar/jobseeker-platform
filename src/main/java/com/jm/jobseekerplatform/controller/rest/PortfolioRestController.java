package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.Portfolio;
import com.jm.jobseekerplatform.model.Subscription;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.service.impl.PortfolioService;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sound.sampled.Port;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/portfolios")
public class PortfolioRestController {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private SeekerProfileService seekerProfileService;

    @RequestMapping("/")
    public List<Portfolio> getAllEmployerProfiles() {
        List<Portfolio> portfolios = portfolioService.getAll();
        return portfolios;
    }

    @RequestMapping("/{portfolioId}")
    public Portfolio getUserById(@PathVariable Long portfolioId){
        Portfolio portfolio = portfolioService.getById(portfolioId);
        return portfolio;
    }

    @RequestMapping("/add")
    @ResponseBody
    public Set<Portfolio> getUserById(@RequestBody SeekerProfile profile){
        long id=profile.getId();
        SeekerProfile updatedProfile = seekerProfileService.getById(profile.getId());
        updatedProfile.setPortfolios(profile.getPortfolios());
        seekerProfileService.update(updatedProfile);
        return updatedProfile.getPortfolios();
    }

//    @RequestMapping(value = "/add", method = RequestMethod.POST)
//    public Portfolio addPortfolio(@RequestParam("profileId") Long profileId,
//                                             @RequestParam("nmae") String name,
//                                             @RequestParam("link") String link,
//                                             @RequestParam("description") String description) {
//        SeekerProfile profile =
//        return new ResponseEntity(HttpStatus.OK);
//    }

    @RequestMapping("/update")
    @ResponseBody
    public Portfolio updatPortfolio(@RequestBody Portfolio portfolio){
        Portfolio updatedPortfolio = portfolioService.getById(portfolio.getId());
        updatedPortfolio.setProjectName(portfolio.getProjectName());
        updatedPortfolio.setDescription(portfolio.getDescription());
        updatedPortfolio.setLink(portfolio.getLink());
        portfolioService.update(updatedPortfolio);
        return updatedPortfolio;
    }


}
