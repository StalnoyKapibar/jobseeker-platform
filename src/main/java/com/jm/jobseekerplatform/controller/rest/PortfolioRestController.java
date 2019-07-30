package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.Portfolio;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.service.impl.PortfolioService;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public Portfolio getUserById(@PathVariable Long portfolioId) {
        Portfolio portfolio = portfolioService.getById(portfolioId);
        return portfolio;
    }

    @RequestMapping("/update")
    public Portfolio updatePortfolio(@RequestParam Long id,
                                     @RequestParam String name,
                                     @RequestParam String link,
                                     @RequestParam String description) {
        Portfolio portfolio = portfolioService.getById(id);
        portfolio.setProjectName(name);
        portfolio.setLink(link);
        portfolio.setDescription(description);
        portfolioService.update(portfolio);
        return portfolio;
    }

    @RequestMapping("/add")
    public Portfolio addPortfolio(@RequestParam Long id,
                                  @RequestParam String name,
                                  @RequestParam String link,
                                  @RequestParam String description) {
        Portfolio portfolio = new Portfolio(name, link, description);
        portfolioService.add(portfolio);
        SeekerProfile profile = seekerProfileService.getById(id);
        profile.getPortfolios().add(portfolio);
        seekerProfileService.update(profile);
        return portfolioService.getById(portfolio.getId());
    }


    @RequestMapping("/delete")
    public SeekerProfile deletePortfolio(@RequestParam Long profileid,
                                         @RequestParam Long portfolioid) {
        SeekerProfile profile = seekerProfileService.getById(profileid);
        Portfolio portfolio = portfolioService.getById(portfolioid);
        profile.getPortfolios().remove(portfolio);
        seekerProfileService.update(profile);
        portfolioService.delete(portfolio);
        return profile;
    }
}
