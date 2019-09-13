package com.jm.jobseekerplatform.dao.impl;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.dto.ResumePageDTO;
import com.jm.jobseekerplatform.model.Resume;
import com.jm.jobseekerplatform.model.SeekerShowName;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.service.impl.SeekerShowNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository("resumeDAO")
public class ResumeDAO extends AbstractDAO<Resume> {

    @Autowired
    private SeekerShowNameService seekerShowNameService;

    public Page<Resume> getAllResumes(int limit, int page) {
        page = (page == 0) ? page : --page;
        Query query = entityManager.createQuery("select r from Resume r", Resume.class);
        long totalElements = (long) entityManager.createQuery("select count(r) from Resume r").getSingleResult();
        int totalPages = getTotalPages(totalElements, limit);
        List<Resume> resumes = query.setFirstResult(page * limit).setMaxResults(limit).getResultList();

        List<Long> rez = new ArrayList<>();
        for (int i = 0; i < resumes.size(); i++) {
            String stringId = String.valueOf(resumes.get(i).getCreatorProfile());
            Long x = Long.parseLong(stringId.replaceAll("\\D+","").trim());
            rez.add(x);
        }
        List<SeekerShowName> seeker = seekerShowNameService.getAllSeekerTestById(rez);

//        for (SeekerShowName st : seekerTest) {
//		    System.out.println(" === " + st.getName() + " " + st.getPatronymic() + " " + st.getSurname());
//	    }
        return new ResumePageDTO(resumes, totalPages, seeker);
    }

    public Page<Resume> getResumesByTag(Set<Tag> tags, int limit, int page) {
        Query query = entityManager.createQuery("select r from Resume r join r.tags t where t in (:tags) group by (r.id)", Resume.class)
                .setParameter("tags", tags);
        long totalElements = (long) entityManager
                .createQuery("select count(distinct r) from Resume r join r.tags t where t in (:tags)")
                .setParameter("tags", tags)
                .getSingleResult();
        int totalPages = getTotalPages(totalElements, limit);
        List<Resume> resumes = query.setFirstResult(page * limit).setMaxResults(limit).getResultList();
        return new ResumePageDTO(resumes, totalPages);
    }

    public Page<Resume> getResumesSortByCity(String city, int limit, int page) {
        page = (page == 0) ? page : --page;
        Query query = entityManager
                .createQuery("select r from Resume r join r.city c join CityDistance cd on c=cd.to where cd.from.name=:city order by cd.distance", Resume.class);
        long totalElements = (long) entityManager.createQuery("select count(r) from Resume r").getSingleResult();
        int totalPages = getTotalPages(totalElements, limit);
        List<Resume> resumes = query.setParameter("city", city)
                .setFirstResult(page * limit)
                .setMaxResults(limit)
                .getResultList();
        return new ResumePageDTO(resumes, totalPages);
    }

    private int getTotalPages(long totalElements, int limit) {
        return (int) (Math.ceil((double) totalElements / (double) limit));
    }
}









//package com.jm.jobseekerplatform.dao.impl;
//
//import com.jm.jobseekerplatform.dao.AbstractDAO;
//import com.jm.jobseekerplatform.dto.ResumePageDTO;
//import com.jm.jobseekerplatform.model.Resume;
//import com.jm.jobseekerplatform.model.Tag;
//import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
//import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Repository;
//import javax.persistence.Query;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//@Repository("resumeDAO")
//public class ResumeDAO extends AbstractDAO<Resume> {
//
//    @Autowired
//    private SeekerProfileService seekerProfileService;
//
//    public Page<Resume> getAllResumes(int limit, int page) {
//        page = (page == 0) ? page : --page;
//        Query query = entityManager.createQuery("select r from Resume r", Resume.class);
//        long totalElements = (long) entityManager.createQuery("select count(r) from Resume r").getSingleResult();
//        int totalPages = getTotalPages(totalElements, limit);
//        List<Resume> resumes = query.setFirstResult(page * limit).setMaxResults(limit).getResultList();
//
//        List<Long> rez = new ArrayList<>();
//        for (int i = 0; i < resumes.size(); i++) {
//            String stringId = String.valueOf(resumes.get(i).getCreatorProfile());
//            Long x = Long.parseLong(stringId.replaceAll("\\D+","").trim());
//            rez.add(x);
//        }
//        List<SeekerProfile> seekerProfile = seekerProfileService.getAllSeekersById(rez);
//
//        for (SeekerProfile sp : seekerProfile) {
//		    System.out.println(" === " + sp.getName() + " " + sp.getPatronymic() + " " + sp.getSurname());
//	    }
//
//        return new ResumePageDTO(resumes, totalPages, seekerProfile);
//    }
//
//    public Page<Resume> getResumesByTag(Set<Tag> tags, int limit, int page) {
//        Query query = entityManager.createQuery("select r from Resume r join r.tags t where t in (:tags) group by (r.id)", Resume.class)
//                .setParameter("tags", tags);
//        long totalElements = (long) entityManager
//                .createQuery("select count(distinct r) from Resume r join r.tags t where t in (:tags)")
//                .setParameter("tags", tags)
//                .getSingleResult();
//        int totalPages = getTotalPages(totalElements, limit);
//        List<Resume> resumes = query.setFirstResult(page * limit).setMaxResults(limit).getResultList();
//        return new ResumePageDTO(resumes, totalPages);
//    }
//
//    public Page<Resume> getResumesSortByCity(String city, int limit, int page) {
//        page = (page == 0) ? page : --page;
//        Query query = entityManager
//                .createQuery("select r from Resume r join r.city c join CityDistance cd on c=cd.to where cd.from.name=:city order by cd.distance", Resume.class);
//        long totalElements = (long) entityManager.createQuery("select count(r) from Resume r").getSingleResult();
//        int totalPages = getTotalPages(totalElements, limit);
//        List<Resume> resumes = query.setParameter("city", city)
//                .setFirstResult(page * limit)
//                .setMaxResults(limit)
//                .getResultList();
//        return new ResumePageDTO(resumes, totalPages);
//    }
//
//    private int getTotalPages(long totalElements, int limit) {
//        return (int) (Math.ceil((double) totalElements / (double) limit));
//    }
//}
