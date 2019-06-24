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
import java.util.*;


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
    private ChatService chatService;

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
        reviewThree.setSeekerProfile(seekerProfileService.getById(2L));
        reviewThree.setEvaluation(4);
        reviewThree.setReviews("Очень низкие зарплаты, уволился через полгода");

        EmployerReviews reviewFour = new EmployerReviews();
        reviewFour.setDateReviews(new Date());
        reviewFour.setSeekerProfile(seekerProfileService.getById(2L));
        reviewFour.setEvaluation(1);
        reviewFour.setReviews("Неадекватное руководство. Уволился через месяц");

        EmployerReviews reviewFive = new EmployerReviews();
        reviewFive.setDateReviews(new Date());
        reviewFive.setSeekerProfile(seekerProfileService.getById(3L));
        reviewFive.setEvaluation(4);
        reviewFive.setReviews("Хорошая контора. Отличный коллектив");

        EmployerReviews reviewSix = new EmployerReviews();
        reviewSix.setDateReviews(new Date());
        reviewSix.setSeekerProfile(seekerProfileService.getById(3L));
        reviewSix.setEvaluation(1);
        reviewSix.setReviews("Все нравилось,но уволился через месяц");

        Set<EmployerReviews> reviewsOne = new HashSet<>();
        reviewsOne.add(reviewOne);
        reviewsOne.add(reviewTwo);

        Set<EmployerReviews> reviewsTwo = new HashSet<>();
        reviewsTwo.add(reviewThree);
        reviewsTwo.add(reviewFour);

        Set<EmployerReviews> reviewsThree = new HashSet<>();
        reviewsThree.add(reviewFive);
        reviewsThree.add(reviewSix);

        Set<EmployerReviews> reviewsFour = new HashSet<>();
        reviewsFour.add(reviewOne);
        reviewsFour.add(reviewFour);

        Set<EmployerReviews> reviewsFive = new HashSet<>();
        reviewsFive.add(reviewThree);
        reviewsFive.add(reviewSix);

        Set<EmployerReviews> reviewsSix = new HashSet<>();
        reviewsSix.add(reviewTwo);
        reviewsSix.add(reviewFive);

        EmployerProfile employerProfileOne = employerProfileService.getById(1L);
        employerProfileOne.setReviews(reviewsOne);
        employerProfileService.update(employerProfileOne);

        EmployerProfile employerProfileTwo = employerProfileService.getById(2L);
        employerProfileTwo.setReviews(reviewsTwo);
        employerProfileService.update(employerProfileTwo);

        EmployerProfile employerProfileThree = employerProfileService.getById(3L);
        employerProfileThree.setReviews(reviewsThree);
        employerProfileService.update(employerProfileThree);

        EmployerProfile employerProfileFour = employerProfileService.getById(4L);
        employerProfileFour.setReviews(reviewsFour);
        employerProfileService.update(employerProfileFour);

        EmployerProfile employerProfileFive = employerProfileService.getById(5L);
        employerProfileFive.setReviews(reviewsFive);
        employerProfileService.update(employerProfileFive);

        EmployerProfile employerProfileSix = employerProfileService.getById(6L);
        employerProfileSix.setReviews(reviewsSix);
        employerProfileService.update(employerProfileSix);
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
        user = new User("admin@mail.ru", userService.encodePassword("admin".toCharArray()), LocalDateTime.now(), role);
        user.setConfirm(true);
        userService.add(user);

        role = userRoleService.findByAuthority("ROLE_EMPLOYER");
        employer = new Employer("employer@mail.ru", userService.encodePassword("employer".toCharArray()), LocalDateTime.now(), role, employerProfileService.getById(1L));
        employer.setConfirm(true);
        employerService.add(employer);

        employer = new Employer("employer2@mail.ru", userService.encodePassword("employer2".toCharArray()), LocalDateTime.now(), role, employerProfileService.getById(2L));
        employer.setConfirm(true);
        employerService.add(employer);

        employer = new Employer("employer3@mail.ru", userService.encodePassword("employer3".toCharArray()), LocalDateTime.now(), role, employerProfileService.getById(3L));
        employer.setConfirm(true);
        employerService.add(employer);

        employer = new Employer("employer4@mail.ru", userService.encodePassword("employer4".toCharArray()), LocalDateTime.now(), role, employerProfileService.getById(4L));
        employer.setConfirm(true);
        employerService.add(employer);

        employer = new Employer("employer5@mail.ru", userService.encodePassword("employer5".toCharArray()), LocalDateTime.now(), role, employerProfileService.getById(5L));
        employer.setConfirm(true);
        employerService.add(employer);

        employer = new Employer("employer6@mail.ru", userService.encodePassword("employer6".toCharArray()), LocalDateTime.now(), role, employerProfileService.getById(6L));
        employer.setConfirm(true);
        employerService.add(employer);

        role = userRoleService.findByAuthority("ROLE_SEEKER");

        seeker = new Seeker("seeker@mail.ru", userService.encodePassword("seeker".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(1L));
        seeker.setConfirm(true);
        seekerService.add(seeker);

        seeker = new Seeker("seeker2@mail.ru", userService.encodePassword("seeker2".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(2L));
        seeker.setConfirm(true);
        seekerService.add(seeker);

        seeker = new Seeker("seeker3@mail.ru", userService.encodePassword("seeker3".toCharArray()), LocalDateTime.now(), role, seekerProfileService.getById(3L));
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
            if (city.equals("Санкт-Петербург")) {
                latitudeY = (float) (0.16 * Math.random()) + 59.88f;
                longitudeX = (float) (0.19 * Math.random()) + 30.24f;
            }
            if (city.equals("Москва")) {
                latitudeY = (float) (0.2 * Math.random()) + 55.66f;
                longitudeX = (float) (0.32 * Math.random()) + 37.45f;
            }

            Point point = new Point(latitudeY, longitudeX);
            pointService.add(point);
            vacancy = new Vacancy(faker.job().title(), city, Math.random() < 0.5, shortDescr, description, Math.random() < 0.5 ? null : (((int) Math.round(Math.random() * 50) + 50) * 1000), Math.random() < 0.5 ? null : (((int) Math.round(Math.random() * 100) + 100) * 1000), randomTags(0L), point);
            vacancy.setState(State.ACCESS);
            vacancyService.add(vacancy);
        }
    }

    public void initEmployerProfiles() {
        BufferedImage image = null;
        EmployerProfile employerProfile;

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
                "Ждем кандидатов с сильным техническим бэкграундом, которые разделяют нашу миссию! ", imageService.resizeLogoEmployer(image), randomVacancies(0L));
        employerProfile.setState(State.ACCESS);
        employerProfileService.add(employerProfile);

        try {
            URL url = new URL("https://0oq.ru/reshebnik-onlajn/ru.onlinemschool.com/pictures/vector/points-to-vector.png");
            image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        employerProfile = new EmployerProfile("Вектор", "www.vector.ru", "Мы хотим ни много ни мало изменить микро-бизнес в России. Поэтому наша цель - создать качественное решение и показать предпринимателям, что их бизнес может больше!", imageService.resizeLogoEmployer(image), randomVacancies(5L));
        employerProfile.setState(State.ACCESS);
        employerProfileService.add(employerProfile);

        for (Long i = 10L; i <= 25L; i++) {
            if (i % 5 == 0) {
                image = getBufferedImage();
                employerProfile = new EmployerProfile(faker.company().name(), faker.company().url(), faker.company().bs(), imageService.resizeLogoEmployer(image), randomVacancies(i));
                employerProfile.setState(State.ACCESS);
                employerProfileService.add(employerProfile);
            }
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
        tagService.add(new Tag("MySQL"));
        tagService.add(new Tag("Thymeleaf"));
        tagService.add(new Tag("OAuth2"));
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

        seekerProfileService.add(new SeekerProfile("Вася", "Игоревич", "Пупкин", "Ищу крутую команду", imageService.resizePhotoSeeker(image), randomTags(0L), portfolios));

        portfolios.clear();
        portfolios.add(portfolioService.getById(3L));
        portfolios.add(portfolioService.getById(4L));
        seekerProfileService.add(new SeekerProfile("Иван", "Игоревич", "Петров", "Ищу крутую команду", imageService.resizePhotoSeeker(image), randomTags(5L), portfolios));

        portfolios.clear();
        portfolios.add(portfolioService.getById(5L));
        portfolios.add(portfolioService.getById(6L));
        seekerProfileService.add(new SeekerProfile("Семен", "Александрович", "Иванов", "Ищу крутую команду", imageService.resizePhotoSeeker(image), randomTags(10L), portfolios));
    }

    private Set<Tag> randomTags(Long position) {
        Set<Tag> tags = new HashSet<>();
        for (Long i = position; i < position + 5; i++) {
            tags.add(tagService.getById(i + 1));
        }
        return tags;
    }

    private Set<Vacancy> randomVacancies(Long position) {
        Set<Vacancy> vacancies = new HashSet<>();
        for (Long i = position; i < position + 5; i++) {
            vacancies.add(vacancyService.getById(i + 1));
        }
        return vacancies;
    }

    public void initChat() {

        Random rnd = new Random();

        for (Long i = 1L; i < 6L; i++) {
            List<ChatMessage> messages = new ArrayList<>();
            for (int k = 0; k < 5; k++) {
                ChatMessage chatMessage = new ChatMessage(faker.gameOfThrones().quote(), userService.findByEmail("admin@mail.ru"), new Date(), false);
                chatMessageService.add(chatMessage);
                messages.add(chatMessage);
            }

            User randomUser = userService.getById(rnd.nextInt(10) + 1L); //todo тут надо одбирать всех, кроме employer
            Vacancy randomVacancy = vacancyService.getById(rnd.nextInt(30) + 1L);

            Chat chat = new ChatWithTopicVacancy(randomUser, randomVacancy);
            chat.setChatMessages(messages);

            chatService.add(chat);
        }
    }


}
