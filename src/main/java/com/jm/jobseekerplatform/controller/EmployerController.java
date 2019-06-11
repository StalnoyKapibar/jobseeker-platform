package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.model.*;
import com.jm.jobseekerplatform.service.impl.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.EmployerService;
import com.jm.jobseekerplatform.service.impl.SeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class EmployerController {
    @Autowired
    private EmployerProfileService employerProfileService;

    @Autowired
    private EmployerService employerService;

    @Autowired
    private SeekerService seekerService;

    @RequestMapping("/employer/{employerProfileId}")
    public String employerProfilePage(@PathVariable Long employerProfileId, Model model, Authentication authentication) {
        EmployerProfile employerProfile = employerProfileService.getById(employerProfileId);
        model.addAttribute("eprofile", employerProfile);
        model.addAttribute("logoimg", Base64.getEncoder().encodeToString(employerProfile.getLogo()));
        if (authentication != null && authentication.isAuthenticated()) {
            Set<String> roles = authentication.getAuthorities().stream().map(grantedAuthority -> ((GrantedAuthority) grantedAuthority).getAuthority()).collect(Collectors.toSet());
            if (roles.contains("ROLE_SEEKER") | roles.contains("ROLE_ADMIN")) {
                Long id = ((User) authentication.getPrincipal()).getId();
                if (roles.contains("ROLE_SEEKER")) {
                    Seeker seeker = (Seeker) seekerService.getById(id);
                    model.addAttribute("seekerId", seeker.getSeekerProfile().getId());
                }
            }
            if (!employerProfile.getReviews().isEmpty()) {
                Set<EmployerReviews> employerReviews = employerProfile.getReviews();
                if (employerReviews.size() < 2) {
                    model.addAttribute("minReviewsEvaluation", employerReviews.iterator().next());
                } else if (employerReviews.size() >= 2) {
                    model.addAttribute("minReviewsEvaluation", employerReviews.stream().sorted().skip(employerReviews.size() - 1).findFirst().orElse(null));
                    model.addAttribute("maxReviewsEvaluation", employerReviews.stream().sorted().findFirst().orElse(null));
                } else {
                    model.addAttribute("reviewStatus", false);
                }
            } else {
                model.addAttribute("reviewStatus", false);
            }
        }
        return "employer";
    }

    @RequestMapping(value = "/admin/employers",method = RequestMethod.GET)
    public String adminPageEmployers(HttpServletRequest request, Model model) {
        int page = 0;
        int size = 10;

        String pageParam = request.getParameter("page");
        String sizeParam = request.getParameter("size");
        String direction = request.getParameter("direction");
        Sort lastVisitSort = new Sort(Sort.Direction.DESC,"date");
        if (direction != null && !direction.isEmpty() && direction.equals("ASC")) {
            lastVisitSort = new Sort(Sort.Direction.ASC, "date");
        }
        if (direction != null && !direction.isEmpty() && direction.equals("DESC")) {
            lastVisitSort = new Sort(Sort.Direction.DESC, "date");
        }

        if (sizeParam != null && !sizeParam.isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }

        if (pageParam != null && !pageParam.isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }
        model.addAttribute("employers", employerService.findAll(PageRequest.of(page, size, lastVisitSort)));
        return "admin_employers";
    }

    @RequestMapping(value = "/admin/employer/{employerId}",method = RequestMethod.GET)
    public String adminPageEmployerToEdit(@PathVariable Long employerId, Model model) {
        Employer employer = employerService.getById(employerId);
        return getString(model, employer);
    }

    private String getString(Model model, Employer employer) {
        model.addAttribute("employer", employer);
        model.addAttribute("employerprof", employer.getEmployerProfile());
        model.addAttribute("photoimg", Base64.getEncoder().encodeToString(employer.getEmployerProfile().getLogo()));
        return "admin_employer_edit";
    }
}
