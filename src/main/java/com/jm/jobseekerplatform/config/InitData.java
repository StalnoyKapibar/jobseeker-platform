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
import java.time.LocalDateTime;
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
        Seeker seeker2;
        Seeker seeker3;
        Seeker seeker4;
        Seeker seeker5;
        Seeker seeker6;
        Seeker seeker7;
        Seeker seeker8;
        Seeker seeker9;
        Seeker seeker10;
        Seeker seeker11;
        Seeker seeker12;
        Seeker seeker13;
        Seeker seeker14;
        Seeker seeker15;
        Seeker seeker16;
        Seeker seeker17;
        Seeker seeker18;
        Seeker seeker19;
        Seeker seeker20;
        Seeker seeker21;
        Seeker seeker22;
        Seeker seeker23;
        Seeker seeker24;

        role = userRoleService.findByAuthority("ROLE_ADMIN");
        user = new User("admin@mail.ru", userService.encodePassword("admin".toCharArray()), LocalDateTime.now(), role);
        user.setConfirm(true);
        userService.add(user);

        role = userRoleService.findByAuthority("ROLE_EMPLOYER");
        employer = new Employer("employer@mail.ru", userService.encodePassword("employer".toCharArray()), LocalDateTime.now(), role, employerProfileService.getById(1L));
        employer.setConfirm(true);
        employerService.add(employer);

        role = userRoleService.findByAuthority("ROLE_SEEKER");
        seeker = new Seeker("seeker@mail.ru", userService.encodePassword("seeker".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(1L));
        seeker.setConfirm(true);
        seekerService.add(seeker);

        seeker2 = new Seeker("seeker2@mail.ru", userService.encodePassword("seeker2".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(1L));
        seeker2.setConfirm(true);
        seekerService.add(seeker2);

        seeker3 = new Seeker("seeker3@mail.ru", userService.encodePassword("seeker3".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(1L));
        seeker3.setConfirm(true);
        seekerService.add(seeker3);

        seeker4 = new Seeker("seeker4@mail.ru", userService.encodePassword("seeker4".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(1L));
        seeker4.setConfirm(true);
        seekerService.add(seeker4);

        seeker5 = new Seeker("seeker5@mail.ru", userService.encodePassword("seeker5".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(1L));
        seeker5.setConfirm(true);
        seekerService.add(seeker5);

        seeker6 = new Seeker("seeker6@mail.ru", userService.encodePassword("seeker6".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(1L));
        seeker6.setConfirm(true);
        seekerService.add(seeker6);

        seeker7 = new Seeker("seeker7@mail.ru", userService.encodePassword("seeker7".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(1L));
        seeker7.setConfirm(true);
        seekerService.add(seeker7);

        seeker8 = new Seeker("seeker8@mail.ru", userService.encodePassword("seeker8".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(1L));
        seeker8.setConfirm(true);
        seekerService.add(seeker8);

        seeker9 = new Seeker("seeker9@mail.ru", userService.encodePassword("seeker9".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(1L));
        seeker9.setConfirm(true);
        seekerService.add(seeker9);

        seeker10 = new Seeker("seeker10@mail.ru", userService.encodePassword("seeker10".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(1L));
        seeker10.setConfirm(true);
        seekerService.add(seeker10);

        seeker11 = new Seeker("seeker11@mail.ru", userService.encodePassword("seeker11".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(1L));
        seeker11.setConfirm(true);
        seekerService.add(seeker11);

        seeker12 = new Seeker("seeker12@mail.ru", userService.encodePassword("seeker12".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(1L));
        seeker12.setConfirm(true);
        seekerService.add(seeker12);

        seeker13 = new Seeker("seeker13@mail.ru", userService.encodePassword("seeker13".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(1L));
        seeker13.setConfirm(true);
        seekerService.add(seeker13);

        seeker14 = new Seeker("seeker14@mail.ru", userService.encodePassword("seeker14".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(1L));
        seeker14.setConfirm(true);
        seekerService.add(seeker14);

        seeker15 = new Seeker("seeker15@mail.ru", userService.encodePassword("seeker15".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(1L));
        seeker15.setConfirm(true);
        seekerService.add(seeker15);

        seeker16 = new Seeker("seeker16@mail.ru", userService.encodePassword("seeker16".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(1L));
        seeker16.setConfirm(true);
        seekerService.add(seeker16);

        seeker17 = new Seeker("seeker17@mail.ru", userService.encodePassword("seeker17".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(1L));
        seeker17.setConfirm(true);
        seekerService.add(seeker17);

        seeker18 = new Seeker("seeker18@mail.ru", userService.encodePassword("seeker18".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(1L));
        seeker18.setConfirm(true);
        seekerService.add(seeker18);

        seeker19 = new Seeker("seeker19@mail.ru", userService.encodePassword("seeker19".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(1L));
        seeker19.setConfirm(true);
        seekerService.add(seeker19);

        seeker20 = new Seeker("seeker20@mail.ru", userService.encodePassword("seeker20".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(1L));
        seeker20.setConfirm(true);
        seekerService.add(seeker20);

        seeker21 = new Seeker("seeker21@mail.ru", userService.encodePassword("seeker21".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(1L));
        seeker21.setConfirm(true);
        seekerService.add(seeker21);

        seeker22 = new Seeker("seeker22@mail.ru", userService.encodePassword("seeker22".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(1L));
        seeker22.setConfirm(true);
        seekerService.add(seeker22);

        seeker23 = new Seeker("seeker23@mail.ru", userService.encodePassword("seeker23".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(1L));
        seeker23.setConfirm(true);
        seekerService.add(seeker23);

        seeker24 = new Seeker("seeker24@mail.ru", userService.encodePassword("seeker24".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(1L));
        seeker24.setConfirm(true);
        seekerService.add(seeker24);

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

        for (int i = 0; i < 30; i++) {
            vacancyService.add(new Vacancy(faker.job().title(), faker.address().city(), Math.random() < 0.5, shortDescr, description, Math.random() < 0.5 ? null : (((int) Math.round(Math.random() * 50) + 50) * 1000), Math.random() < 0.5 ? null : (((int) Math.round(Math.random() * 100) + 100) * 1000), randomTags(), State.ACCESS));
        }
    }

    public void initEmployerProfiles() {
        BufferedImage image = null;
        Set<Vacancy> vacancies = new HashSet<>();
        vacancies.add(vacancyService.getById(1L));
        vacancies.add(vacancyService.getById(2L));
        try {
            URL url = new URL("https://wiki.godville.net/images/2/25/%D0%A0%D0%BE%D0%B3%D0%B0_%D0%B8_%D0%9A%D0%BE%D0%BF%D1%8B%D1%82%D0%B0_%28%D0%BB%D0%BE%D0%B3%D0%BE%29.png");
            image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        employerProfileService.add(new EmployerProfile("Рога и копыта", "www.roga.ru", "Мы продуктовая компания, которая разрабатывает высокотехнологичные продукты в области электротранспорта, роботизации, автоматизации и биотехнологий.\n" +
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
                "Ждем кандидатов с сильным техническим бэкграундом, которые разделяют нашу миссию! ", imageService.resizeLogoEmployer(image), vacancies));

        vacancies.clear();
        vacancies.add(vacancyService.getById(3L));
        try {
            URL url = new URL("https://0oq.ru/reshebnik-onlajn/ru.onlinemschool.com/pictures/vector/points-to-vector.png");
            image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        employerProfileService.add(new EmployerProfile("Вектор", "www.vector.ru", "Мы хотим ни много ни мало изменить микро-бизнес в России. Поэтому наша цель - создать качественное решение и показать предпринимателям, что их бизнес может больше!", imageService.resizeLogoEmployer(image), vacancies));

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
        //seekerProfileService.add(new SeekerProfile("Gq", "Po", "Qw", "Ищу крутую команду", imageService.resizePhotoSeeker(image), tags, portfolios));
    }

    public Set<Tag> randomTags() {
        Set<Tag> tags = new HashSet<>();
        for (int i = 0; i < Math.round(Math.random() * 3) + 1; i++) {
            tags.add(tagService.getById(Math.round(Math.random() * 11) + 1));
        }
        return tags;
    }

}
