package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.model.users.UserEmployer;
import com.jm.jobseekerplatform.model.users.UserSeeker;
import com.jm.jobseekerplatform.service.impl.EmployerService;
import com.jm.jobseekerplatform.service.impl.SeekerService;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

@Controller
public class AdminController {

    @Autowired
    private EmployerService employerService;

    @Autowired
    private SeekerService seekerService;

    @Autowired
    private VacancyService vacancyService;

    @RequestMapping("/admin")
    public String adminPage() {
        return "admin";
    }

    @RequestMapping("/admin/addUser")
    public String adminAddUser() {
        return "admin_addUser";
    }

    @RequestMapping("/admin/vacancies")
    public String adminPageVacancies() {
        return "admin_vacancies";
    }

    @RequestMapping("/admin/chats")
    public String adminPageChats() {
        return "admin_chats";
    }

    @RequestMapping(value = "/admin/employers", method = RequestMethod.GET)
    public String adminPageEmployers(HttpServletRequest request, Model model) {
        int page = 0;
        int size = 10;

        String pageParam = request.getParameter("page");
        String sizeParam = request.getParameter("size");
        String direction = request.getParameter("direction");

        Sort lastVisitSort = new Sort(Sort.Direction.DESC, "date");
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

        Page<UserEmployer> userEmployers = employerService.findAll(PageRequest.of(page, size, lastVisitSort));

        model.addAttribute("userEmployers", userEmployers);
        return "admin_employers";
    }

    @RequestMapping(value = "/admin/employer/{userEmployerId}", method = RequestMethod.GET)
    public String adminPageEmployerToEdit(@PathVariable Long userEmployerId, Model model) {
        UserEmployer userEmployer = employerService.getById(userEmployerId);

        model.addAttribute("userEmployer", userEmployer);
        model.addAttribute("profileEmployer", userEmployer.getProfile());
        model.addAttribute("vacancies", vacancyService.getAllByEmployerProfileId(userEmployer.getProfile().getId()));
        model.addAttribute("photoimg", Base64.getEncoder().encodeToString(userEmployer.getProfile().getLogo()));

        return "admin_employer_edit";
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

        model.addAttribute("userSeekers", seekerService.findAll(PageRequest.of(page, size, lastVisitSort)));

        return "admin_seekers";
    }

    @RequestMapping(value = "/admin/seeker/{seekerId}", method = RequestMethod.GET)
    public String adminPageSeekerToEdit(@PathVariable Long seekerId, Model model) {
        UserSeeker userSeeker = seekerService.getById(seekerId);

        model.addAttribute("userSeeker", userSeeker);
        model.addAttribute("profileSeeker", userSeeker.getProfile());
        model.addAttribute("photoimg", Base64.getEncoder().encodeToString(userSeeker.getProfile().getPhoto()));

        return "admin_seeker_edit";
    }
}