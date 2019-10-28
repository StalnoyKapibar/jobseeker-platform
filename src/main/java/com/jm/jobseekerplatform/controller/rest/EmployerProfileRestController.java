package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.dto.UserTimerDTO;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import com.jm.jobseekerplatform.service.impl.profiles.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.users.EmployerUserService;
import com.jm.jobseekerplatform.service.impl.users.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/employerprofiles")
public class EmployerProfileRestController {

    @Autowired
    private EmployerProfileService employerProfileService;

    @Autowired
    VacancyService vacancyService;

    @Autowired
    UserService userService;

    @Autowired
    EmployerUserService employerUserService;

    @RequestMapping("/")
    public List<EmployerProfile> getAllEmployerProfiles() {
        List<EmployerProfile> employerprofiles = employerProfileService.getAll();
        return employerprofiles;
    }

    @RequestMapping("/{employerProfileId:\\d+}")
    public EmployerProfile getEmployerProfileById(@PathVariable Long employerProfileId){
        EmployerProfile employerProfile = employerProfileService.getById(employerProfileId);
        return employerProfile;
    }

    @RequestMapping(value = "/companies", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<EmployerProfile>> getCompanies() {
        return ResponseEntity.ok(employerProfileService.getAll());
    }

    @PostMapping("/update")
    public EmployerProfile getSearchUserProfiles(@RequestBody String jsonReq) throws JSONException {
        JSONObject jsonProfile = new JSONObject(jsonReq);
        EmployerProfile updatedProfile = employerProfileService.getById(jsonProfile.getLong("id"));
        updatedProfile.setCompanyName(jsonProfile.getString("companyname"));
        updatedProfile.setWebsite(jsonProfile.getString("site"));
        updatedProfile.setDescription(jsonProfile.getString("description"));
        employerProfileService.update(updatedProfile);
        return updatedProfile;
    }

    @RequestMapping(value = "/update_image", method = RequestMethod.POST)
    public String updateImage(@RequestParam(value = "id") long id,
                              @RequestParam(value = "image") MultipartFile img) {
        employerProfileService.updatePhoto(id, img);
        return employerProfileService.getById(id).getEncoderPhoto();
    }

    @GetMapping("/employer_timer")
    public List<UserTimerDTO> employerTimer() {
        List<EmployerProfile> profiles = employerProfileService.getProfilesExpNotNull();
        List<UserTimerDTO> userTimerDTO = new ArrayList<>();
        for (EmployerProfile ep : profiles) {
            User user = employerUserService.getUserByEmployerProfile(ep);
            Date expireDate = ep.getExpiryBlock();
            userTimerDTO.add(createUserTimerDTO(expireDate, user));
        }
        return userTimerDTO;
    }

    public UserTimerDTO createUserTimerDTO(Date expireDate, User user) {
        UserTimerDTO utDTO = new UserTimerDTO();
        utDTO.setUserId(user.getId());
        String str = expireDate.toString();
        int count;
        switch(str.length()) {
            case (21):
                count = 2;
                break;
            case (22):
                count = 3;
                break;
            default:
                count = 4;
                break;
        }
        utDTO.setTime(str.substring(0, str.length() - count));
        return utDTO;
    }

}
