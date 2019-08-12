package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.service.impl.ImageService;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import com.jm.jobseekerplatform.service.impl.profiles.EmployerProfileService;
import javafx.scene.chart.ValueAxis;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api/employerprofiles")
public class EmployerProfileRestController {

    @Autowired
    private EmployerProfileService employerProfileService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private VacancyService vacancyService;

    @RequestMapping("/")
    public List<EmployerProfile> getAllEmployerProfiles() {
        List<EmployerProfile> employerprofiles = employerProfileService.getAll();
        return employerprofiles;
    }

    @RequestMapping("/{employerProfileId:\\d+}")
    public EmployerProfile getEmployerProfileById(@PathVariable Long employerProfileId) {
        EmployerProfile employerProfile = employerProfileService.getById(employerProfileId);
        return employerProfile;
    }

    @RequestMapping(value = "/block/{vacancyId:\\d+}", method = RequestMethod.POST)
    public void blockEmployerProfile(@PathVariable("vacancyId") Long id, @RequestBody int periodInDays) {
        EmployerProfile employerProfile = employerProfileService.getById(id);
        if (periodInDays == 0) {
            employerProfileService.blockPermanently(employerProfile);
        }
        if (periodInDays > 0 && periodInDays < 15) {
            employerProfileService.blockTemporary(employerProfile, periodInDays);
        }
    }

    @PostMapping("/update")
    @ResponseBody
    public EmployerProfile getSearchUserProfiles(@RequestBody String jsonReq) throws JSONException {
        JSONObject jsonData = new JSONObject(jsonReq);
        JSONObject jsonProfile = new JSONObject(String.valueOf(jsonData.getJSONObject("profile")));
        EmployerProfile updatedProfile = employerProfileService.getById(jsonProfile.getLong("id"));
        updatedProfile.setCompanyName(jsonProfile.getString("companyname"));
        updatedProfile.setWebsite(jsonProfile.getString("site"));
        updatedProfile.setDescription(jsonProfile.getString("description"));
        JSONArray vacArr = jsonData.getJSONArray("vacancies");
        Set<Vacancy> profileVacansies = vacancyService.getAllByEmployerProfileId(jsonProfile.getLong("id"));
        profileVacansies.forEach(n -> n.setPublicationPosition(null));
        Vacancy vacancy;
        for (int i = 0; i < vacArr.length(); i++) {
            vacancy = vacancyService.getById(vacArr.getJSONObject(i).getLong("id"));
            vacancy.setPublicationPosition(vacArr.getJSONObject(i).getInt("position"));
            vacancyService.update(vacancy);
        }
        employerProfileService.update(updatedProfile);
        return updatedProfile;
    }

    @RequestMapping(value = "/update_image", method = RequestMethod.POST)
    public String updateImage(@RequestParam(value = "id") long id,
                              @RequestParam(value = "image") MultipartFile img) {
        EmployerProfile profile = employerProfileService.getById(id);
        if (img != null) {
            BufferedImage image = null;
            try {
                image = ImageIO.read(new File());
                profile.setLogo(imageService.resizePhotoSeeker(image));
                employerProfileService.update(profile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return Base64.getEncoder().encodeToString(profile.getLogo());
    }
}
