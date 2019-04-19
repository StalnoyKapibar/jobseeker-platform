package com.jm.jobseekerplatform.config;

import com.jm.jobseekerplatform.model.*;
import com.jm.jobseekerplatform.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
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


    public void initData() {
        initTags();
        initUserRoles();
        initVacancies();
        initPortfolio();
        initEmployerProfiles();
        initSeekerProfile();
        initUsers();
    }

    public void initUserRoles() {
        userRoleService.add(new UserRole("ROLE_ADMIN"));
        userRoleService.add(new UserRole("ROLE_USER"));
        userRoleService.add(new UserRole("ROLE_EMPLOYER"));
        userRoleService.add(new UserRole("ROLE_SEEKER"));
    }

    public void initUsers() {
        UserRole roleAdmin = userRoleService.findByAuthority("ROLE_ADMIN");
        userService.add(new User("admin", userService.encodePassword("admin").toCharArray(), "admin@mail.ru", roleAdmin));

        UserRole roleUser = userRoleService.findByAuthority("ROLE_USER");
        userService.add(new User("user", userService.encodePassword("user").toCharArray(), "user@mail.ru", roleUser));

        UserRole roleEmployer = userRoleService.findByAuthority("ROLE_EMPLOYER");
        employerService.add(new Employer("employer", userService.encodePassword("employer").toCharArray(), "employer@mail.ru", roleEmployer, employerProfileService.getById(1L)));

        UserRole roleSeeker = userRoleService.findByAuthority("ROLE_SEEKER");
        seekerService.add(new Seeker("seeker", userService.encodePassword("seeker").toCharArray(), "seeker@mail.ru", roleSeeker, seekerProfileService.getById(1L)));

    }

    public void initVacancies() {
        vacancyService.add(new Vacancy("Инженер-погромист", "Москва", false,"Платим деньги за работу", null, 120000,new HashSet<Tag>(Arrays.asList(tagService.getById(1L),tagService.getById(2L),tagService.getById(3L),tagService.getById(4L)))));
        vacancyService.add(new Vacancy("Java программист", "Москва", false,"Обязанности:\n" +
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
                "Мы ищем талантливых специалистов! Если Вы уверены в себе и хотите заниматься любимым делом профессионально, пишите нам! Мы хотим видеть людей, готовых работать над серьезными проектами и добиваться отличных результатов. Мы предлагаем интересную работу в дружном и профессиональном коллективе, в котором ценится работа каждого. Вы можете стать частью нашей команды!", 110000, null,new HashSet<Tag>(Arrays.asList(tagService.getById(2L),tagService.getById(4L)))));
        vacancyService.add(new Vacancy("Java Developer", "Санкт-Петербург", false,"Участвовать в проектировании сервисов, оптимизировать высоконагруженный проект, внедрять новые технологии и Big Data хранилищ. Участвовать в формулировании и декомпозиции продуктовых...\n" +
                "Имеете опыт работы со Spring Boot, Spring Data JPA, Rabbit MQ. Знаете и понимаете шаблоны проектирования, клиент-серверные технологии.", 90000, 120000, new HashSet<Tag>(Arrays.asList(tagService.getById(1L),tagService.getById(3L)))));
    }

    public void initEmployerProfiles() {
        BufferedImage image = null;
        Set<Vacancy> vacancies = new HashSet<>();
        vacancies.add(vacancyService.getById(1L));
        vacancies.add(vacancyService.getById(2L));
        try {
            URL url = new URL("https://wiki.godville.net/images/2/2d/RiK-lens.png");
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

    public void initPortfolio(){
        portfolioService.add(new Portfolio("Jobseeker-platform","https://github.com/StalnoyKapibar/jobseeker-platform","Создавал модели, сервисы. Использовал Java 8, Spring"));
        portfolioService.add(new Portfolio("SportGames","https://github.com/romanX1/SportGames/","Прикручивал Spring Security. Использовал Java 8, Spring"));
    }

    public void initTags(){
        tagService.add(new Tag("Java 8"));
        tagService.add(new Tag("Spring"));
        tagService.add(new Tag("Git"));
        tagService.add(new Tag("Maven"));
    }

    public void initSeekerProfile(){
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

        seekerProfileService.add(new SeekerProfile("Вася Игоревич Пупкин", "Ищу крутую команду", imageService.resizePhotoSeeker(image), tags, portfolios));
    }

}
