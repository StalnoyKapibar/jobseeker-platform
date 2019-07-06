package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.Portfolio;
import com.jm.jobseekerplatform.service.impl.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/portfolios")
public class PortfolioRestController {

    @Autowired
    private PortfolioService portfolioService;

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
}
