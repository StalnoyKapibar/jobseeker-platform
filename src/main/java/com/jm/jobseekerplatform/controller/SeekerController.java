package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.model.Seeker;
import com.jm.jobseekerplatform.model.SeekerProfile;
import com.jm.jobseekerplatform.service.impl.SeekerProfileService;
import com.jm.jobseekerplatform.service.impl.SeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

@Controller
public class SeekerController {

    @Autowired
    private SeekerService seekerService;

    @Autowired
    private SeekerProfileService seekerProfileService;

    @RequestMapping("/seeker/{seekerProfileId}")
    public String seekerProfilePage(@PathVariable Long seekerProfileId, Model model) {
        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        model.addAttribute("sprofile", seekerProfile);
        model.addAttribute("photoimg", Base64.getEncoder().encodeToString(seekerProfile.getPhoto()));
        return "seeker";
    }

    @RequestMapping(value = "/admin/seekers", method = RequestMethod.GET)
    public String adminPageSeekers(HttpServletRequest request, Model model) {
        int page = 0;
        int size = 10;
        Sort lastVisitSort = new Sort(Sort.Direction.DESC, "date");
        String direction = request.getParameter("direction");
        String pageParam = request.getParameter("page");
        String sizeParam = request.getParameter("size");
        if (direction != null && !direction.isEmpty() && direction.equals("DESC")) {
            lastVisitSort = new Sort(Sort.Direction.DESC, "date");
        }

        if (direction != null && !direction.isEmpty() && direction.equals("ASC")) {
            lastVisitSort = new Sort(Sort.Direction.ASC, "date");
        }

        if (pageParam != null && !pageParam.isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }

        if (sizeParam != null && !sizeParam.isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        model.addAttribute("seekers", seekerService.findAll(PageRequest.of(page, size, lastVisitSort)));
        return "admin_seekers";
    }

    @RequestMapping(value = "/admin/seeker/{seekerId}", method = RequestMethod.GET)
    public String adminPageSeekerToEdit(@PathVariable Long seekerId, Model model) {
        Seeker seeker = seekerService.getById(seekerId);
        return getString(model, seeker);
    }

    private String getString(Model model, Seeker seeker) {
        model.addAttribute("seeker", seeker);
        model.addAttribute("seekerprof", seeker.getSeekerProfile());
        model.addAttribute("photoimg", Base64.getEncoder().encodeToString(seeker.getSeekerProfile().getPhoto()));
        return "admin_seeker_edit";
    }
}
