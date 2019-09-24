package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.model.users.EmployerUser;
import com.jm.jobseekerplatform.model.users.SeekerUser;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.TagService;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import com.jm.jobseekerplatform.service.impl.users.EmployerUserService;
import com.jm.jobseekerplatform.service.impl.users.SeekerUserService;
import com.jm.jobseekerplatform.service.impl.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private EmployerUserService employerUserService;

    @Autowired
    private SeekerUserService seekerUserService;

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @RequestMapping("/admin")
    public String adminPage() {
        return "admin/admin";
    }

    @RequestMapping("/admin/addUser")
    public String adminAddUser() {
        return "admin/admin_addUser";
    }

    @RequestMapping("/admin/vacancies")
    public String adminPageVacancies() {
        return "admin/admin_vacancies";
    }

    @RequestMapping("/admin/chats/all")
    public String adminPageChatsAll() {
        return "admin/admin_chats_all";
    }

    @RequestMapping("/admin/chats/my")
    public String adminPageChatsMy(Authentication authentication, Model model) {
        Long userId = ((User) (authentication.getPrincipal())).getId();
        User user = userService.getById(userId);
        model.addAttribute("profileId", user.getProfile().getId());
        return "admin/admin_chats_my";
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
            size = Integer.parseInt(sizeParam);
        }

        if (pageParam != null && !pageParam.isEmpty()) {
            page = Integer.parseInt(pageParam) - 1;
        }

        Page<EmployerUser> employerUsers = employerUserService.findAll(PageRequest.of(page, size, lastVisitSort));

        model.addAttribute("employerUsers", employerUsers);

        return "admin/admin_employers";
    }

    @RequestMapping(value = "/admin/employer/{userEmployerId}", method = RequestMethod.GET)
    public String adminPageEmployerToEdit(@PathVariable Long userEmployerId, Model model) {
        EmployerUser employerUser = employerUserService.getById(userEmployerId);

        model.addAttribute("employerUser", employerUser);
        model.addAttribute("employerProfile", employerUser.getProfile());
        model.addAttribute("vacancies", vacancyService.getAllByEmployerProfileId(employerUser.getProfile().getId()));
        if(employerUser.getProfile().getLogo() != null) {
            model.addAttribute("photoimg", Base64.getEncoder().encodeToString(employerUser.getProfile().getLogo()));
        }

        return "admin/admin_employer_edit";
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

        model.addAttribute("seekerUsers", seekerUserService.findAll(PageRequest.of(page, size, lastVisitSort)));

        return "admin/admin_seekers";
    }

    @RequestMapping(value = "/admin/seeker/{seekerUserId}", method = RequestMethod.GET)
    public String adminPageSeekerToEdit(@PathVariable Long seekerUserId, Model model) {
        SeekerUser seekerUser = seekerUserService.getById(seekerUserId);

        model.addAttribute("seekerUser", seekerUser);
        model.addAttribute("seekerProfile", seekerUser.getProfile());
        if(seekerUser.getProfile().getLogo() != null){
            model.addAttribute("photoimg", Base64.getEncoder().encodeToString(seekerUser.getProfile().getLogo()));
        }


        return "admin/admin_seeker_edit";
    }

    @RequestMapping(value = "/admin/tags", method = RequestMethod.GET)
    public String UsersViewPage(Model model) {
        List<Tag> tags = tagService.getSortedAll();
        model.addAttribute("tags", tags);
        return "admin/admin_tags";
    }
}