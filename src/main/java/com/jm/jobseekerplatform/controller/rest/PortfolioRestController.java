package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.Portfolio;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.service.impl.PortfolioService;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public Portfolio getPorfolioById(@PathVariable Long portfolioId) {
        Portfolio portfolio = portfolioService.getById(portfolioId);
        return portfolio;
    }

    @RequestMapping("/add/{id}")
    public Set<Portfolio> addPortfolio(@PathVariable("id") Long profileId,
                                       @RequestBody Portfolio newPortfolio) {
        portfolioService.add(newPortfolio);
        SeekerProfile updatedProfile = seekerProfileService.getById(profileId);
        Set<Portfolio> newPortfolioSet = updatedProfile.getPortfolios();
        newPortfolioSet.add(newPortfolio);
        updatedProfile.setPortfolios(newPortfolioSet);
        seekerProfileService.update(updatedProfile);
        return updatedProfile.getPortfolios();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Set<Portfolio> deletePortfolio(@RequestParam("profileId") Long profileId,
                                       @RequestParam("portfolioId") Long portfolioId) {
        SeekerProfile updatedProfile = seekerProfileService.getById(profileId);
        Set<Portfolio> newPortfolio = updatedProfile.getPortfolios();
        Portfolio deletedPortfolio = portfolioService.getById(portfolioId);
        newPortfolio.remove(deletedPortfolio);
        seekerProfileService.update(updatedProfile);
        return updatedProfile.getPortfolios();
    }

    @RequestMapping("/update")
    public Portfolio updatePortfolio(@RequestBody Portfolio portfolio) {
        Portfolio updatedPortfolio = portfolioService.getById(portfolio.getId());
        updatedPortfolio.setProjectName(portfolio.getProjectName());
        updatedPortfolio.setDescription(portfolio.getDescription());
        updatedPortfolio.setLink(portfolio.getLink());
        portfolioService.update(updatedPortfolio);
        return updatedPortfolio;
    }

}
