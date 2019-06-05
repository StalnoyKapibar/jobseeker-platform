package com.jm.jobseekerplatform.config;

import com.github.javafaker.Faker;
import com.jm.jobseekerplatform.model.*;
import com.jm.jobseekerplatform.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Component
public class InitData {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserService userService;

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private EmployerProfileService employerProfileService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private TagService tagService;

    @Autowired
    private SeekerProfileService seekerProfileService;

    @Autowired
    private EmployerService employerService;

    @Autowired
    private SeekerService seekerService;

    @Autowired
    private EmployerReviewsService employerReviewsService;

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private PointService pointService;

    private Faker faker = new Faker(new Locale("ru"));



    public void initData() {
        initTags();
        initUserRoles();
        initVacancies();
        initPortfolio();
        initEmployerProfiles();
        initSeekerProfile();
        initUsers();
        initReviews();
        initChat();
    }

    public void initReviews() {
        EmployerReviews reviewOne = new EmployerReviews();
        reviewOne.setDateReviews(new Date());
        reviewOne.setEvaluation(4);
        reviewOne.setSeekerProfile(seekerProfileService.getById(1L));
        reviewOne.setReviews("Хорошая контора. Отличный коллектив, только директор придурковатый");

        EmployerReviews reviewTwo = new EmployerReviews();
        reviewTwo.setDateReviews(new Date());
        reviewTwo.setSeekerProfile(seekerProfileService.getById(1L));
        reviewTwo.setEvaluation(1);
        reviewTwo.setReviews("Неадекватное руководство. Уволился через месяц");

        EmployerReviews reviewThree = new EmployerReviews();
        reviewThree.setDateReviews(new Date());
        reviewThree.setSeekerProfile(seekerProfileService.getById(1L));
        reviewThree.setEvaluation(1);
        reviewThree.setReviews("Неадекватное руководство. Уволился через месяц");

        EmployerReviews reviewFour = new EmployerReviews();
        reviewFour.setDateReviews(new Date());
        reviewFour.setSeekerProfile(seekerProfileService.getById(1L));
        reviewFour.setEvaluation(4);
        reviewFour.setReviews("Хорошая контора. Отличный коллектив, только директор придурковатый");

        Set<EmployerReviews> reviewsOne = new HashSet<>();
        reviewsOne.add(reviewOne);
        reviewsOne.add(reviewTwo);

        Set<EmployerReviews> reviewsTwo = new HashSet<>();
        reviewsTwo.add(reviewThree);
        reviewsTwo.add(reviewFour);

        EmployerProfile employerProfileOne = employerProfileService.getById(1L);
        employerProfileOne.setReviews(reviewsOne);
        employerProfileService.update(employerProfileOne);

        EmployerProfile employerProfileTwo = employerProfileService.getById(2L);
        employerProfileTwo.setReviews(reviewsTwo);
        employerProfileService.update(employerProfileTwo);
    }

    public void initUserRoles() {
        userRoleService.add(new UserRole("ROLE_ADMIN"));
        userRoleService.add(new UserRole("ROLE_EMPLOYER"));
        userRoleService.add(new UserRole("ROLE_SEEKER"));
    }

    public void initUsers() {
        UserRole role;
        User user;
        Employer employer;
        Seeker seeker;

        role = userRoleService.findByAuthority("ROLE_ADMIN");
        user = new User("admin@mail.ru", userService.encodePassword("admin".toCharArray()), role);
        user.setConfirm(true);
        userService.add(user);

        role = userRoleService.findByAuthority("ROLE_EMPLOYER");
        employer = new Employer("employer@mail.ru", userService.encodePassword("employer".toCharArray()), role, employerProfileService.getById(1L));
        employer.setConfirm(true);
        employerService.add(employer);

        role = userRoleService.findByAuthority("ROLE_SEEKER");
        seeker = new Seeker("seeker@mail.ru", userService.encodePassword("seeker".toCharArray()), role, seekerProfileService.getById(1L));
        seeker.setConfirm(true);
        seekerService.add(seeker);
    }

    public void initVacancies() {
        String shortDescr = "Ищем талантливого разработчика, умеющего все и немного больше";
        String description = "Обязанности:\n" +
                "\n" +
                "Разработка новых модулей системы\n" +
                "Перевод существующих модулей на микросервисную архитектуру\n" +
                "Требования:\n" +
                "Высшее образование\n" +
                "Опыт работы с мультипоточностью (multithreading)\n" +
                "Владение основными паттернами проектирования\n" +
                "Знание и понимание RESTful-протоколов\n" +
                "Умение быстро разбираться в чужом коде\n" +
                "Английский язык (на уровне intermediate)\n" +
                "Опыт работы в проектах с Docker, Kubernetes;\n" +
                "Условия:\n" +
                "Белая заработная плата, официальное трудоустройство\n" +
                "Гибкий график работы\n" +
                "Лояльное отношение к сотрудникам\n" +
                "Дружный коллектив\n" +
                "Дополнительная информация:\n" +
                "Мы ищем талантливых специалистов! Если Вы уверены в себе и хотите заниматься любимым делом профессионально, пишите нам! Мы хотим видеть людей, готовых работать над серьезными проектами и добиваться отличных результатов. Мы предлагаем интересную работу в дружном и профессиональном коллективе, в котором ценится работа каждого. Вы можете стать частью нашей команды!";

        Vacancy vacancy;
        String city;
        Float latitudeY = 0f;
        Float longitudeX = 0f;
        for (int i = 0; i < 30; i++) {
            city = Math.random() < 0.5 ? "Москва" : "Санкт-Петербург";
            if (city.equals("Санкт-Петербург")){
                latitudeY = (float)(0.16 * Math.random()) + 59.88f;
                longitudeX = (float)(0.19 * Math.random()) + 30.24f;
            }
            if (city.equals("Москва")){
                latitudeY = (float)(0.2 * Math.random()) + 55.66f;
                longitudeX = (float)(0.32 * Math.random()) + 37.45f;
            }

            Point point = new Point(latitudeY, longitudeX);
            pointService.add(point);
            vacancy = new Vacancy(faker.job().title(), city, Math.random() < 0.5, shortDescr, description, Math.random() < 0.5 ? null : (((int) Math.round(Math.random() * 50) + 50) * 1000), Math.random() < 0.5 ? null : (((int) Math.round(Math.random() * 100) + 100) * 1000), randomTags(), point);
            vacancy.setState(State.ACCESS);
            vacancyService.add(vacancy);
        }
    }

    public void initEmployerProfiles() {
        BufferedImage image = null;
        Set<Vacancy> vacancies = new HashSet<>();
        EmployerProfile employerProfile;

        vacancies.add(vacancyService.getById(1L));
        vacancies.add(vacancyService.getById(2L));
        try {
            URL url = new URL("https://wiki.godville.net/images/2/25/%D0%A0%D0%BE%D0%B3%D0%B0_%D0%B8_%D0%9A%D0%BE%D0%BF%D1%8B%D1%82%D0%B0_%28%D0%BB%D0%BE%D0%B3%D0%BE%29.png");
            image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        employerProfile = new EmployerProfile("Рога и копыта", "www.roga.ru", "Мы продуктовая компания, которая разрабатывает высокотехнологичные продукты в области электротранспорта, роботизации, автоматизации и биотехнологий.\n" +
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
                "Ждем кандидатов с сильным техническим бэкграундом, которые разделяют нашу миссию! ", imageService.resizeLogoEmployer(image), vacancies);
        employerProfile.setState(State.ACCESS);
        employerProfileService.add(employerProfile);

        vacancies.clear();
        vacancies.add(vacancyService.getById(3L));
        try {
            URL url = new URL("https://0oq.ru/reshebnik-onlajn/ru.onlinemschool.com/pictures/vector/points-to-vector.png");
            image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        employerProfile = new EmployerProfile("Вектор", "www.vector.ru", "Мы хотим ни много ни мало изменить микро-бизнес в России. Поэтому наша цель - создать качественное решение и показать предпринимателям, что их бизнес может больше!", imageService.resizeLogoEmployer(image), vacancies);
        employerProfile.setState(State.ACCESS);
        employerProfileService.add(employerProfile);
    }

    public void initPortfolio() {
        portfolioService.add(new Portfolio("Jobseeker-platform", "https://github.com/StalnoyKapibar/jobseeker-platform", "Создавал модели, сервисы. Использовал Java 8, Spring"));
        portfolioService.add(new Portfolio("SportGames", "https://github.com/romanX1/SportGames/", "Прикручивал Spring Security. Использовал Java 8, Spring"));
    }

    public void initTags() {
        tagService.add(new Tag("Java 8"));
        tagService.add(new Tag("Spring Framework"));
        tagService.add(new Tag("Git"));
        tagService.add(new Tag("Maven"));
        tagService.add(new Tag("Apache Tomcat"));
        tagService.add(new Tag("Java Servlets"));
        tagService.add(new Tag("JSF"));
        tagService.add(new Tag("PostgreSQL"));
        tagService.add(new Tag("Oracle Pl/SQL"));
        tagService.add(new Tag("JMS"));
        tagService.add(new Tag("Azure"));
        tagService.add(new Tag("React"));
    }

    public void initSeekerProfile() {
        BufferedImage image = null;
        Set<Portfolio> portfolios = new HashSet<>();
        portfolios.add(portfolioService.getById(1L));
        portfolios.add(portfolioService.getById(2L));
        try {
            URL url = new URL("https://zapravka-kartridzhej-spb.ru/wp-content/uploads/2016/10/spezialist-zapravka-kartridzhej-spb-10.png");
            image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<Tag> tags = new HashSet<>();
        tags.add(tagService.getById(1L));
        tags.add(tagService.getById(2L));
        tags.add(tagService.getById(3L));
        tags.add(tagService.getById(4L));

        seekerProfileService.add(new SeekerProfile("Вася", "Игоревич", "Пупкин", "Ищу крутую команду", imageService.resizePhotoSeeker(image), tags, portfolios));
    }

    public Set<Tag> randomTags() {
        Set<Tag> tags = new HashSet<>();
        for (int i = 0; i < Math.round(Math.random() * 3) + 1; i++) {
            tags.add(tagService.getById(Math.round(Math.random() * 11) + 1));
        }
        return tags;
    }

    public void initChat() {

        for (Long i=1L; i<6L; i++){
            Set<ChatMessage> messages = new HashSet<>();
            for (int k=0; k<5; k++) {
                ChatMessage chatMessage = new ChatMessage(faker.gameOfThrones().quote(), "admin@mail.ru", new Date(), "false", "true");
                chatMessageService.add(chatMessage);
                messages.add(chatMessage);
            }
            Vacancy vacancy = vacancyService.getById(i);
            vacancy.setChatMessages(messages);
            vacancyService.update(vacancy);
        }
    }


}
