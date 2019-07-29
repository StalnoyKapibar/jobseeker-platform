package com.jm.jobseekerplatform.controller.rest;

import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.service.impl.ImageService;
import com.jm.jobseekerplatform.service.impl.profiles.EmployerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/employerprofiles")

public class EmployerProfileRestController {

    @Autowired
    private EmployerProfileService employerProfileService;

    @Autowired
    private ImageService imageService;


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
    public EmployerProfile editProfile(@RequestParam(value = "id") long id,
                                       @RequestParam(value = "companyname", required = false) String companyName,
                                       @RequestParam(value = "website", required = false) String website,
                                       @RequestParam(value = "description", required = false) String description) {
        EmployerProfile profile = employerProfileService.getById(id);
        if (companyName != null) {
            profile.setCompanyName(companyName);
        }
        if (website != null) {
            profile.setWebsite(website);
        }
        if (description != null) {
            profile.setDescription(description);
        }
        employerProfileService.update(profile);
        return profile;
    }

    @RequestMapping(value = "/update_image", method = RequestMethod.POST)
    public String updateImage(@RequestParam(value = "id") long id,
                              @RequestParam(value = "image") MultipartFile img) throws IOException {
        EmployerProfile profile = employerProfileService.getById(id);
        if (img != null) {
            profile.setLogo(imageService.resizePhotoSeeker(ImageIO.read(new ByteArrayInputStream(img.getBytes()))));
            employerProfileService.update(profile);
        }
        return Base64.getEncoder().encodeToString(profile.getLogo());
    }
}
