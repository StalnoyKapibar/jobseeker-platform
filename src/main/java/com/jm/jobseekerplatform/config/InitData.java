package com.jm.jobseekerplatform.config;

import com.github.javafaker.Faker;
import com.jm.jobseekerplatform.model.*;
import com.jm.jobseekerplatform.model.chats.Chat;
import com.jm.jobseekerplatform.model.chats.ChatMessage;
import com.jm.jobseekerplatform.model.chats.ChatWithTopicVacancy;
import com.jm.jobseekerplatform.model.comments.Comment;
import com.jm.jobseekerplatform.model.profiles.AdminProfile;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.users.AdminUser;
import com.jm.jobseekerplatform.model.users.EmployerUser;
import com.jm.jobseekerplatform.model.users.SeekerUser;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.*;
import com.jm.jobseekerplatform.service.impl.chats.ChatMessageService;
import com.jm.jobseekerplatform.service.impl.chats.ChatService;
import com.jm.jobseekerplatform.service.impl.comments.CommentService;
import com.jm.jobseekerplatform.service.impl.profiles.AdminProfileService;
import com.jm.jobseekerplatform.service.impl.profiles.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.profiles.ProfileService;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import com.jm.jobseekerplatform.service.impl.users.EmployerUserService;
import com.jm.jobseekerplatform.service.impl.users.SeekerUserService;
import com.jm.jobseekerplatform.service.impl.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Component
public class InitData {

    public static final String WEB_IMG_DIR = "src/main/resources/static/img";

    @Autowired
    private UserService userService;

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private AdminProfileService adminProfileService;

    @Autowired
    private EmployerProfileService employerProfileService;

    @Autowired
    private SeekerProfileService seekerProfileService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private TagService tagService;

    @Autowired
    private EmployerUserService employerUserService;

    @Autowired
    private SeekerUserService seekerUserService;

    @Autowired
    private EmployerReviewsService employerReviewsService;

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private PointService pointService;

    @Autowired
    private CityService cityService;

    @Autowired
    private CityDistanceService cityDistanceService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private ReviewVoteService reviewVoteService;

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private JobExperienceService jobExperienceService;

    @Autowired
    private StoredProcedureService storedProcedureService;

    @Autowired
    private CommentService commentService;

    private Faker faker = new Faker(new Locale("ru"));

    private Random rnd = new Random();

    public void initData() {
        storedProcedureService.createSortVacancyProcedure();

        initCities();
        initTags();
        initPortfolio();

        initAdminProfile();
        initEmployerProfiles();
        initVacancies();

        initSeekerProfile();
        initUsers();

        initReviews();
        initChat();
        initNews();
        initJobExperience();
        initResumes();
        initComments();
    }

    private void initNews() {
        for (int i = 0; i < 60; i++) {
            News news = new News(faker.witcher().witcher(), faker.lorem().characters(), null, LocalDateTime.now());
            newsService.add(news);
        }

        List<News> newsList = newsService.getAll();
        for (int i = 0; i < newsList.size(); i++) {
            if (i < 10) {
                newsList.get(i).setAuthor(employerProfileService.getById(2L));
                newsList.get(i).setTags(randomTags(1L));
                newsService.update(newsList.get(i));
            }
            if (i >= 10 && i < 20) {
               newsList.get(i).setAuthor(employerProfileService.getById(3L));
                newsList.get(i).setTags(randomTags(2L));
                newsService.update(newsList.get(i));
            }
            if (i >= 20 && i < 30) {
                newsList.get(i).setAuthor(employerProfileService.getById(4L));
                newsList.get(i).setTags(randomTags(3L));
                newsService.update(newsList.get(i));
            }
            if (i >= 30 && i < 40) {
                newsList.get(i).setAuthor(employerProfileService.getById(5L));
                newsList.get(i).setTags(randomTags(4L));
                newsService.update(newsList.get(i));
            }
            if (i >= 40 && i < 50) {
                newsList.get(i).setAuthor(employerProfileService.getById(6L));
                newsList.get(i).setTags(randomTags(5L));
                newsService.update(newsList.get(i));
            }
            if (i >= 50 && i < 60) {
                newsList.get(i).setAuthor(employerProfileService.getById(7L));
                newsList.get(i).setTags(randomTags(6L));
                newsService.update(newsList.get(i));
            }
        }

    }

    public void initReviews() {
        SeekerProfile seekerProfile = seekerProfileService.getById(8L);
        EmployerReviews reviewOne = new EmployerReviews(seekerProfile, seekerProfile.getFullName(), "Хорошая контора. Отличный коллектив, только директор придурковатый", new Date(),
                4, seekerProfile);

        EmployerReviews reviewTwo = new EmployerReviews(seekerProfile, seekerProfile.getFullName(), "Неадекватное руководство. Уволился через месяц", new Date(),
                1, seekerProfile);

        seekerProfile = seekerProfileService.getById(9L);
        EmployerReviews reviewThree = new EmployerReviews(seekerProfile, seekerProfile.getFullName(), "Очень низкие зарплаты, уволился через полгода", new Date(),
                2, seekerProfile);

        EmployerReviews reviewFour = new EmployerReviews(seekerProfile, seekerProfile.getFullName(), "Неадекватное руководство. Уволился через месяц", new Date(),
                1, seekerProfile);

        seekerProfile = seekerProfileService.getById(10L);
        EmployerReviews reviewFive = new EmployerReviews(seekerProfile, seekerProfile.getFullName(), "Хорошая контора. Отличный коллектив", new Date(),
                4, seekerProfile);

        EmployerReviews reviewSix = new EmployerReviews(seekerProfile, seekerProfile.getFullName(), "Все нравилось,но уволился через месяц", new Date(),
                3, seekerProfile);

        Set<EmployerReviews> reviewsOne = new HashSet<>();
        reviewsOne.add(reviewOne);
        reviewsOne.add(reviewThree);

        Set<EmployerReviews> reviewsTwo = new HashSet<>();
        reviewsTwo.add(reviewOne);
        reviewsTwo.add(reviewFive);

        Set<EmployerReviews> reviewsThree = new HashSet<>();
        reviewsThree.add(reviewTwo);
        reviewsThree.add(reviewFour);

        Set<EmployerReviews> reviewsFour = new HashSet<>();
        reviewsFour.add(reviewTwo);
        reviewsFour.add(reviewSix);

        Set<EmployerReviews> reviewsFive = new HashSet<>();
        reviewsFive.add(reviewOne);
        reviewsFive.add(reviewSix);

        Set<EmployerReviews> reviewsSix = new HashSet<>();
        reviewsSix.add(reviewThree);
        reviewsSix.add(reviewFive);

        EmployerProfile employerProfileOne = employerProfileService.getById(2L);
        employerProfileOne.setReviews(reviewsOne);
        employerProfileService.update(employerProfileOne);

        EmployerProfile employerProfileTwo = employerProfileService.getById(3L);
        employerProfileTwo.setReviews(reviewsTwo);
        employerProfileService.update(employerProfileTwo);

        EmployerProfile employerProfileThree = employerProfileService.getById(4L);
        employerProfileThree.setReviews(reviewsThree);
        employerProfileService.update(employerProfileThree);

        EmployerProfile employerProfileFour = employerProfileService.getById(5L);
        employerProfileFour.setReviews(reviewsFour);
        employerProfileService.update(employerProfileFour);

        EmployerProfile employerProfileFive = employerProfileService.getById(6L);
        employerProfileFive.setReviews(reviewsFive);
        employerProfileService.update(employerProfileFive);

        EmployerProfile employerProfileSix = employerProfileService.getById(7L);
        employerProfileSix.setReviews(reviewsSix);
        employerProfileService.update(employerProfileSix);
    }

    public void initUsers() {
        User user;
        EmployerUser employerUser;
        SeekerUser seekerUser;

        user = new AdminUser("admin@mail.ru", userService.encodePassword("admin".toCharArray()), LocalDateTime.now(), adminProfileService.getById(1L));
        user.setConfirm(true);
        userService.add(user);

        employerUser = new EmployerUser("employer@mail.ru", userService.encodePassword("employer".toCharArray()), LocalDateTime.now(), employerProfileService.getById(2L));
        employerUser.setConfirm(true);
        employerUserService.add(employerUser);

        employerUser = new EmployerUser("employer2@mail.ru", userService.encodePassword("employer2".toCharArray()), LocalDateTime.now(), employerProfileService.getById(3L));
        employerUser.setConfirm(true);
        employerUserService.add(employerUser);

        employerUser = new EmployerUser("employer3@mail.ru", userService.encodePassword("employer3".toCharArray()), LocalDateTime.now(), employerProfileService.getById(4L));
        employerUser.setConfirm(true);
        employerUserService.add(employerUser);

        employerUser = new EmployerUser("employer4@mail.ru", userService.encodePassword("employer4".toCharArray()), LocalDateTime.now(), employerProfileService.getById(5L));
        employerUser.setConfirm(true);
        employerUserService.add(employerUser);

        employerUser = new EmployerUser("employer5@mail.ru", userService.encodePassword("employer5".toCharArray()), LocalDateTime.now(), employerProfileService.getById(6L));
        employerUser.setConfirm(true);
        employerUserService.add(employerUser);

        employerUser = new EmployerUser("employer6@mail.ru", userService.encodePassword("employer6".toCharArray()), LocalDateTime.now(), employerProfileService.getById(7L));
        employerUser.setConfirm(true);
        employerUserService.add(employerUser);

        seekerUser = new SeekerUser("seeker@mail.ru", userService.encodePassword("seeker".toCharArray()), LocalDateTime.now(), seekerProfileService.getById(8L));
        seekerUser.setConfirm(true);
        seekerUserService.add(seekerUser);

        seekerUser = new SeekerUser("seeker2@mail.ru", userService.encodePassword("seeker2".toCharArray()), LocalDateTime.now(), seekerProfileService.getById(9L));
        seekerUser.setConfirm(true);
        seekerUserService.add(seekerUser);

        seekerUser = new SeekerUser("seeker3@mail.ru", userService.encodePassword("seeker3".toCharArray()), LocalDateTime.now(), seekerProfileService.getById(10L));
        seekerUser.setConfirm(true);
        seekerUserService.add(seekerUser);
    }

    public void initVacancies() {
        String shortDescr = "Ищем талантливого разработчика, умеющего все и немного больше";
        String description = "Обязанности:<br>" +
                "<br>" +
                "Разработка новых модулей системы<br>" +
                "Перевод существующих модулей на микросервисную архитектуру<br>" +
                "Требования:<br>" +
                "Высшее образование<br>" +
                "Опыт работы с мультипоточностью (multithreading)<br>" +
                "Владение основными паттернами проектирования<br>" +
                "Знание и понимание RESTful-протоколов<br>" +
                "Умение быстро разбираться в чужом коде<br>" +
                "Английский язык (на уровне intermediate)<br>" +
                "Опыт работы в проектах с Docker, Kubernetes;<br>" +
                "Условия:<br>" +
                "Белая заработная плата, официальное трудоустройство<br>" +
                "Гибкий график работы<br>" +
                "Лояльное отношение к сотрудникам<br>" +
                "Дружный коллектив<br>" +
                "Дополнительная информация:<br>" +
                "Мы ищем талантливых специалистов! Если Вы уверены в себе и хотите заниматься любимым делом профессионально, пишите нам! Мы хотим видеть людей, готовых работать над серьезными проектами и добиваться отличных результатов. Мы предлагаем интересную работу в дружном и профессиональном коллективе, в котором ценится работа каждого. Вы можете стать частью нашей команды!";

        Vacancy vacancy;
        Point point;
        City city;
        List<City> cities = cityService.getAll();
        for (int i = 0; i < 30; i++) {
            city = cities.get(rnd.nextInt(cities.size()));
            point = city.getPoint();

            vacancy = new Vacancy(
                    getRandomEmployerProfile(),
                    faker.job().title(),
                    city,
                    Math.random() < 0.5,
                    shortDescr,
                    description,
                    Math.random() < 0.5 ? null : (((int) Math.round(Math.random() * 50) + 50) * 1000), //salaryMin
                    Math.random() < 0.5 ? null : (((int) Math.round(Math.random() * 100) + 100) * 1000), //salaryMax
                    randomTags(0L),
                    point,
                    new Date(System.currentTimeMillis()));

            vacancy.setState(State.ACCESS);
            vacancyService.add(vacancy);
        }
    }

    private void initAdminProfile() {
        BufferedImage image = null;
        try {
            File sourceImage = new File(WEB_IMG_DIR + "/LOGO_001.jpg");
            image = ImageIO.read(sourceImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdminProfile adminProfile = new AdminProfile();
        adminProfile.setState(State.ACCESS);
        adminProfile.setLogo(imageService.resizeLogoEmployer(image));
        adminProfileService.add(adminProfile);
    }

    public void initEmployerProfiles() {
        BufferedImage image = null;
        EmployerProfile employerProfile;

        try {
            File sourceImage = new File(WEB_IMG_DIR + "/roga-kopyta.png");
            image = ImageIO.read(sourceImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String description = "Мы продуктовая компания, которая разрабатывает высокотехнологичные продукты в области электротранспорта, роботизации, автоматизации и биотехнологий.\n" +
                "В России базируется отдел исследований и разработок. \n" +
                "Стек: Java8, J2ee, Spring. Клиентская часть: ES6, React, React Native. Облака AWS, Docker, NodeJS (+ Express/Koa/Hapi).\n" +
                "Миссия - улучшать жизнь людей с помощью технологий.\n" +
                "Что мы предлагаем:\n" +
                "- Футуристические в масштабах планеты проекты. \n" +
                "- Офисы в центре Москвы и Санкт-Петербурга. \n" +
                "- Частичный \"home office\".\n" +
                "- Профессиональная команда Тим Лидеров с четким видением развития продуктов.\n" +
                "- Новые технологии без legacy кода. \n" +
                "- Открытая атмосфера, без корпоративного \"булшита\". \n" +
                "- Официальное оформление по ТК РФ. \n" +
                "Ждем кандидатов с сильным техническим бэкграундом, которые разделяют нашу миссию! ";

        employerProfile = new EmployerProfile("Рога и копыта", "www.roga.ru", description, imageService.resizeLogoEmployer(image));
        employerProfile.setState(State.ACCESS);
        employerProfileService.add(employerProfile);

        try {
            File sourceImage = new File(WEB_IMG_DIR + "/points-to-vector.png");
            image = ImageIO.read(sourceImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        employerProfile = new EmployerProfile("Вектор", "www.vector.ru", "Мы хотим ни много ни мало изменить микро-бизнес в России. Поэтому наша цель - создать качественное решение и показать предпринимателям, что их бизнес может больше!", imageService.resizeLogoEmployer(image));
        employerProfile.setState(State.ACCESS);
        employerProfileService.add(employerProfile);

        for (long i = 0L; i <= 3L; i++) {
            image = getBufferedImage();
            employerProfile = new EmployerProfile(faker.company().name(), faker.company().url(), faker.company().bs(), imageService.resizeLogoEmployer(image));
            employerProfile.setState(State.ACCESS);
            employerProfileService.add(employerProfile);
        }
    }

    private BufferedImage getBufferedImage() {
        BufferedImage image = null;
        try {
            URL url = new URL(faker.company().logo());
            image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void initPortfolio() {
        portfolioService.add(new Portfolio("Jobseeker-platform", "https://github.com/StalnoyKapibar/jobseeker-platform", "Создавал модели, сервисы. Использовал Java 8, Spring"));
        portfolioService.add(new Portfolio("SportGames", "https://github.com/romanX1/SportGames/", "Прикручивал Spring Security. Использовал Java 8, Spring"));
        portfolioService.add(new Portfolio("MusicEmulator", "https://github.com/musicX1/MusicEmulator/", "Прикручивал Spring Boot. Использовал Maven, Hibernate"));
        portfolioService.add(new Portfolio("OverReal", "https://github.com/OverReal/", "Создавал 3d модели дл дополненной реальности"));
        portfolioService.add(new Portfolio("EpicGames", "https://github.com/Xbox6/EpicGames/", "Прикручивал Sockets. Использовал Java 8, Spring"));
        portfolioService.add(new Portfolio("BestBrowser", "https://github.com/BB1/BestBrowser/", " Использовал Java 8, Spring"));
    }

    public void initTags() {
        boolean verified = true;
        tagService.add(new Tag("Java 8", verified));
        tagService.add(new Tag("Spring Framework", verified));
        tagService.add(new Tag("Git", verified));
        tagService.add(new Tag("Maven", verified));
        tagService.add(new Tag("Apache Tomcat", verified));
        tagService.add(new Tag("Java Servlets", verified));
        tagService.add(new Tag("JSF", verified));
        tagService.add(new Tag("PostgreSQL", verified));
        tagService.add(new Tag("Oracle Pl/SQL", verified));
        tagService.add(new Tag("JMS", verified));
        tagService.add(new Tag("Azure", verified));
        tagService.add(new Tag("React", verified));
        tagService.add(new Tag("MySQL", verified));
        tagService.add(new Tag("Thymeleaf", verified));
        tagService.add(new Tag("OAuth2", verified));
    }

    public void initSeekerProfile() {
        BufferedImage image = null;
        Set<Portfolio> portfolios = new HashSet<>();
        portfolios.add(portfolioService.getById(1L));
        portfolios.add(portfolioService.getById(2L));
        try {
            File sourceImage = new File(WEB_IMG_DIR + "/spezialist-zapravka-kartridzhej-spb-10.png");
            image = ImageIO.read(sourceImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<Vacancy> vacancies = new HashSet<>(vacancyService.getAll());

        seekerProfileService.add(new SeekerProfile("Вася", "Игоревич", "Пупкин", "Ищу крутую команду", imageService.resizePhotoSeeker(image), randomTags(0L), portfolios, vacancies, new HashSet<>()));


        portfolios.clear();
        portfolios.add(portfolioService.getById(3L));
        portfolios.add(portfolioService.getById(4L));
        seekerProfileService.add(new SeekerProfile("Иван", "Игоревич", "Петров", "Ищу крутую команду", imageService.resizePhotoSeeker(image), randomTags(5L), portfolios, vacancies, new HashSet<>()));

        portfolios.clear();
        portfolios.add(portfolioService.getById(5L));
        portfolios.add(portfolioService.getById(6L));
        seekerProfileService.add(new SeekerProfile("Семен", "Александрович", "Иванов", "Ищу крутую команду", imageService.resizePhotoSeeker(image), randomTags(10L), portfolios, vacancies, new HashSet<>()));
    }

    private Set<Tag> randomTags(Long position) {
        Set<Tag> tags = new HashSet<>();
        int countAllTags = tagService.getAll().size();
        for (Long i = position; i < position + 5L; i++) {
            tags.add(tagService.getById(rnd.nextInt(countAllTags) + 1L));
        }
        return tags;
    }

    private void initChat() {
        Random rnd = new Random();
        List<SeekerProfile> profiles = seekerProfileService.getAll();

        for (long i = 1L; i < 6L; i++) {
            List<ChatMessage> messages = new ArrayList<>();
            Vacancy randomVacancy = vacancyService.getById(rnd.nextInt(30) + 1L);
            Profile chatCreator = getRandomProfileExceptWithId(randomVacancy.getCreatorProfile().getId());
            Set<User> members = new HashSet<>();
            SeekerProfile profile = profiles.get(rnd.nextInt(profiles.size()));
            for (int k = 0; k < 5; k++) {
                ChatMessage chatMessage = new ChatMessage(faker.gameOfThrones().quote(), profile, new Date());
                ChatMessage chatMessage2 = new ChatMessage(faker.gameOfThrones().quote(), chatCreator, new Date());
                chatMessageService.add(chatMessage);
                chatMessageService.add(chatMessage2);
                messages.add(chatMessage);
                messages.add(chatMessage2);
                members.add(seekerUserService.getByProfileId(profile.getId()));
            }
            List<User> chatMembers = new ArrayList<>(members);
            chatMembers.add(employerUserService.getByProfileId(randomVacancy.getCreatorProfile().getId()));

            Chat chat = new ChatWithTopicVacancy(chatCreator, chatMembers, randomVacancy);
            chat.setChatMessages(messages);

            chatService.add(chat);
        }
    }

    private Profile getRandomProfileExceptWithId(Long exceptProfileId) {
        boolean ready = false;

        int amountOfProfiles = profileService.getAll().size();
        int randomId = -1;

        while (!ready) {
            randomId = rnd.nextInt(amountOfProfiles) + 1;
            if (randomId != exceptProfileId) {
                ready = true;
            }
        }

        return profileService.getById((long) randomId);
    }

    private EmployerProfile getRandomEmployerProfile() {
        List<EmployerProfile> all = employerProfileService.getAll();
        return all.get(rnd.nextInt(all.size()));
    }

    private SeekerProfile getRandomSeekerProfile() {
        List<SeekerProfile> all = seekerProfileService.getAll();
        return all.get(rnd.nextInt(all.size()));
    }

    private void initCities() {
        cityService.initCity("Москва", new Point(55.752030F, 37.633685F));
        cityService.initCity("Санкт-Петербург", new Point(59.943122F, 30.276844F));
        cityService.initCity("Ярославль", new Point(57.650630F, 39.860908F));
        cityService.initCity("Казань", new Point(55.825853F, 49.117538F));
        cityService.initCity("Екатеринбург", new Point(56.825312F, 60.608923F));
        cityService.initCity("Нижний Новгород", new Point(56.299846F, 43.904104F));
    }

    private void initJobExperience() {
        for (int i = 0; i < 20; i++) {
            Date date = new Date();
            JobExperience jobExperience = new JobExperience(date,
                    new Date(date.getTime() + (24 * 60 * 60 * 1000)),//+1 день
                    faker.job().title(),
                    faker.job().position(),
                    faker.witcher().quote());
            jobExperienceService.add(jobExperience);
        }
    }

    private void initResumes() {
        Set<Resume> resumes = new HashSet<>();
        Set<JobExperience> jobExperiences = new HashSet<>();
        List<City> cities = cityService.getAll();
        List<JobExperience> jobExperienceList = jobExperienceService.getAll();
        Set<Long> usedProfiles = new HashSet<>();
        Resume resume;
        Point point;
        City city;
        for (int i = 0, m = 0; i < seekerProfileService.getAll().size(); i++) {
            SeekerProfile seekerProfile = getRandomSeekerProfile();
            while (!usedProfiles.add(seekerProfile.getId())){
              seekerProfile = getRandomSeekerProfile();
            }
            for (int j = 0; j < 4; j++){
                city = cities.get(rnd.nextInt(cities.size()));
                point = city.getPoint();
                jobExperiences.add(jobExperienceList.get(m++));
                resume = new Resume(
                        seekerProfile,
                        faker.job().title(),
                        randomTags(0L),
                        Math.random() < 0.5 ? null : (((int) Math.round(Math.random()
                                * 50) + 50) * 1000), //salaryMin
                        Math.random() < 0.5 ? null : (((int) Math.round(Math.random()
                                * 100) + 100) * 1000), //salaryMax
                        jobExperiences,
                        city,
                        point);
                resumes.add(resume);
                resumeService.add(resume);
                jobExperiences.clear();
            }
            seekerProfile.setResumes(resumes);
            seekerProfileService.update(seekerProfile);
            resumes.clear();
        }
    }

    private void initComments() {
        Profile profile1 = profileService.getById(8L);
        Profile profile2 = profileService.getById(9L);
        Profile profile3 = profileService.getById(10L);
        List<News> newsList = newsService.getAll();
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        Comment comment = null;
        for (News n : newsList) {
            comment = new Comment("Отличная новость", n, profile1, dateTime);
            commentService.add(comment);
            comment = new Comment("Плохая новость", n, profile2, dateTime);
            commentService.add(comment);
            comment = new Comment("Тупая новость", n, profile3, dateTime);
            commentService.add(comment);
        }
    }
}
