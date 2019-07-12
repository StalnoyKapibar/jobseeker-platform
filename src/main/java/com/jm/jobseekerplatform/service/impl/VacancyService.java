package com.jm.jobseekerplatform.service.impl;

<<<<<<< HEAD
import com.google.maps.errors.ApiException;
=======
>>>>>>> c4aab85a3b7b18a227459e49349585f8ff7fcbf5
import com.jm.jobseekerplatform.dao.VacancyDaoI;
import com.jm.jobseekerplatform.dao.impl.VacancyDAO;
import com.jm.jobseekerplatform.dto.PageVacancyDTO;
import com.jm.jobseekerplatform.model.Point;
import com.jm.jobseekerplatform.model.State;
import com.jm.jobseekerplatform.model.Tag;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.service.AbstractService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

<<<<<<< HEAD
import java.io.IOException;
import java.util.*;
=======
import java.util.Calendar;
import java.util.Date;
>>>>>>> c4aab85a3b7b18a227459e49349585f8ff7fcbf5
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("vacancyService")
@Transactional
public class VacancyService extends AbstractService<Vacancy> {

    @Autowired
    private VacancyDAO dao;

    @Autowired
    private VacancyDaoI vacancyDaoI;

    @Autowired
    private TagService tagService;

    @Autowired
    private PointService pointService;

    private Pattern pattern;
    private Matcher matcher;

<<<<<<< HEAD
=======
    public Set<Vacancy> getAllByEmployerProfileId(Long id, int limit) {
        return dao.getAllByEmployerProfileId(id, limit);
    }

    public Set<Vacancy> getAllByEmployerProfileId(Long id) {
        return dao.getAllByEmployerProfileId(id);
    }

    public Page<Vacancy> findAll(Pageable pageable) {
        return vacancyDaoI.findAll(pageable);
    }

    public Page<Vacancy> findAllByTags(Set<Tag> tags, Pageable pageable) {
        return vacancyDaoI.findAllByTags(tags, pageable);
    }

    public Set<Vacancy> getByTags(Set<Tag> tags, int limit) {
        return dao.getAllByTags(tags, limit);
    }

>>>>>>> c4aab85a3b7b18a227459e49349585f8ff7fcbf5
    public void blockPermanently(Vacancy vacancy) {
        vacancy.setState(State.BLOCK_PERMANENT);
        vacancy.setExpiryBlock(null);
        dao.update(vacancy);
    }

    public void blockTemporary(Vacancy vacancy, int periodInDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, periodInDays);
        Date expiryBlockDate = calendar.getTime();

        vacancy.setState(State.BLOCK_TEMPORARY);
        vacancy.setExpiryBlock(expiryBlockDate);
        dao.update(vacancy);
    }

    public void blockOwn(Vacancy vacancy) {
        vacancy.setState(State.BLOCK_OWN);
        vacancy.setExpiryBlock(null);
        dao.update(vacancy);
    }

    public void unblock(Vacancy vacancy) {
        vacancy.setState(State.ACCESS);
        vacancy.setExpiryBlock(null);
        dao.update(vacancy);
    }

    public int deletePermanentBlockVacancies() {
        return dao.deletePermanentBlockVacancies();
    }

    public int deleteExpiryBlockVacancies() {
        return dao.deleteExpiryBlockVacancies();
    }

    public boolean validateVacancy(Vacancy vacancy) {
        String headline_pattern = "^[A-Za-z0-9А-Яа-я ()\\-]{3,100}$";
        String city_pattern = "^[A-Za-z0-9А-Яа-я ()\\-]{3,100}$";
        boolean isCorrect;

        if (vacancy.getHeadline().isEmpty() || vacancy.getCity().isEmpty() || vacancy.getShortDescription().isEmpty() || vacancy.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Some fields is empty");
        }
        pattern = Pattern.compile(headline_pattern);
        matcher = pattern.matcher(vacancy.getHeadline());
        isCorrect = matcher.matches();

        pattern = Pattern.compile(city_pattern);
        matcher = pattern.matcher(vacancy.getCity());
        isCorrect &= matcher.matches();

        isCorrect &= vacancy.getTags().size() > 0;
        return isCorrect;
    }

    public void addNewVacancyFromRest(Vacancy vacancy) {
        vacancy.setState(State.NO_ACCESS);
        Point point = vacancy.getCoordinates();
        pointService.add(point);
        vacancy.setCoordinates(point);
        Set<Tag> matchedTags = tagService.matchTagsByName(vacancy.getTags());
        vacancy.setTags(matchedTags);
        dao.add(vacancy);
    }

    public Page<Vacancy> findAllVacanciesByPoint(Point point, int limit, int page) throws InterruptedException, ApiException, IOException {
        List<Point> points = pointService.getAll();
        List<Point> pointsInCity = new ArrayList<>();
        List<Point> pointsOutside = new ArrayList<>();
        List<Vacancy> vacancies = new ArrayList<>();

        for (int i=0; i<points.size(); i++) {
            if (pointService.comparePoints(point, points.get(i))) {
                pointsInCity.add(points.get(i));
            } else {
                pointsOutside.add(points.get(i));
            }
        }

        for (Point p : pointsInCity) {
            Vacancy vacancy = vacancyDaoI.findVacancyByCoordinates(p);
            vacancies.add(vacancy);
        }
        return new PageVacancyDTO(vacancies, vacancies.size());
    }

    public Page<Vacancy> getVacanciesByTagsAndByPoint(Point point, Set<Tag> tags, int limit, int page) {
        return null;
    }


//
//    public Page<Vacancy> getAllVacanciesBySortPoint(Point point, int limit, int page) throws IOException, JSONException {
//        List<Point> points = pointService.getAll();
//        List<Point> sortPoints = pointService.comparePoints(point, points);
//
//        List<Vacancy> vacancies = findVacanciesByPoint(sortPoints, limit, page);
//        int totalPages = vacancyDaoI.findAll().size()/limit;
//        return new PageVacancyDTO(vacancies, totalPages);
//    }
//
//    public Page<Vacancy> getVacanciesByTagsBySortPoint(Point point, Set<Tag> tags, int limit, int page) throws IOException, JSONException {
//        List<Point> points = dao.getPointsByTags(tags);
//        List<Point> sortPoints = pointService.comparePoints(point, points);
//        List<Vacancy> sortVacancies = findVacanciesByPoint(sortPoints, limit, page);
//        int totalPages = points.size()/limit;
//        return new PageVacancyDTO(sortVacancies, totalPages);
//    }
//
//    private List<Vacancy> findVacanciesByPoint(List<Point> sortPoints, int limit, int page) {
//        List<Vacancy> vacancies = new ArrayList<>();
//        int firstResult = limit * page;
//        int maxResult = firstResult + limit;
//
//        for (int i = firstResult; i < maxResult; i++) {
//            Point p = sortPoints.get(i);
//            Vacancy v = vacancyDaoI.findVacancyByCoordinates(p);
//            vacancies.add(v);
//        }
//        return vacancies;
//    }
}