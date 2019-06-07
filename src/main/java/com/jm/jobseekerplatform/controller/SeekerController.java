package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.model.Seeker;
import com.jm.jobseekerplatform.model.SeekerProfile;
import com.jm.jobseekerplatform.service.impl.SeekerProfileService;
import com.jm.jobseekerplatform.service.impl.SeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @RequestMapping(value = "/admin/seekers",method = RequestMethod.GET)
    public String adminPageSeekers(HttpServletRequest request, Model model,
                                   @PageableDefault(sort = {"date"}, direction = Sort.Direction.DESC) Pageable pageable) {
        int page = 0;
        int size = 10;
        Sort lastVisitSort = null;
        if (request.getParameter("direction") != null &&
                !request.getParameter("direction").isEmpty() &&
                request.getParameter("direction").equals("DESC")) {
            lastVisitSort = new Sort(Sort.Direction.DESC, "date");
        }
        if (request.getParameter("direction") != null &&
                !request.getParameter("direction").isEmpty() &&
                request.getParameter("direction").equals("ASC")) {
            lastVisitSort = new Sort(Sort.Direction.ASC, "date");
        }

        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        model.addAttribute("seekers", seekerService.findAll(PageRequest.of(page, size, lastVisitSort != null ? lastVisitSort : pageable.getSort())));
        return "admin_seekers";
    }

    @RequestMapping(value = "/admin/seeker/{seekerId}",method = RequestMethod.GET)
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

    @RequestMapping(value = "/admin/seeker/editPhoto", method = RequestMethod.POST)
    public String adminPageSeekerPhotoToEdit(@RequestParam("seekerId") Long seekerId, @RequestParam("file") MultipartFile file, Model model) {
        Seeker seeker = seekerService.getById(seekerId);
        if (!file.isEmpty()) {
            try {
                byte[] photo = file.getBytes();
                seeker.getSeekerProfile().setPhoto(photo);
                seekerProfileService.update(seeker.getSeekerProfile());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return getString(model, seeker);
    }

}
