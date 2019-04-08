package com.jm.jobseekerplatform.config;

import com.jm.jobseekerplatform.model.EmployerProfile;
import com.jm.jobseekerplatform.model.User;
import com.jm.jobseekerplatform.model.UserRole;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
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

    public void initData() {
        initUserRoles();
        initUsers();
        initVacancies();
        initEmployerProfiles();
    }

    public void initUserRoles() {
        userRoleService.add(new UserRole("ROLE_ADMIN"));
        userRoleService.add(new UserRole("ROLE_USER"));
        userRoleService.add(new UserRole("ROLE_EMPLOYER"));
    }

    public void initUsers() {
        UserRole roleAdmin = userRoleService.findByAuthority("ROLE_ADMIN");
        userService.add(new User("admin", userService.encodePassword("admin"), "admin@mail.ru", roleAdmin, null));

        UserRole roleUser = userRoleService.findByAuthority("ROLE_USER");
        userService.add(new User("user", userService.encodePassword("user"), "user@mail.ru", roleUser, null));

        UserRole roleEmployer = userRoleService.findByAuthority("ROLE_EMPLOYER");
        userService.add(new User("employer", userService.encodePassword("employer"), "employer@mail.ru", roleEmployer, null));
    }

    public void initVacancies() {
        vacancyService.add(new Vacancy("Инженер-погромист", "Москва", false,"Платим деньги за работу", 100000, 120000));
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
                "Мы ищем талантливых специалистов! Если Вы уверены в себе и хотите заниматься любимым делом профессионально, пишите нам! Мы хотим видеть людей, готовых работать над серьезными проектами и добиваться отличных результатов. Мы предлагаем интересную работу в дружном и профессиональном коллективе, в котором ценится работа каждого. Вы можете стать частью нашей команды!", 110000, 140000));
        vacancyService.add(new Vacancy("Java Developer", "Санкт-Петербург", false,"Участвовать в проектировании сервисов, оптимизировать высоконагруженный проект, внедрять новые технологии и Big Data хранилищ. Участвовать в формулировании и декомпозиции продуктовых...\n" +
                "Имеете опыт работы со Spring Boot, Spring Data JPA, Rabbit MQ. Знаете и понимаете шаблоны проектирования, клиент-серверные технологии.", 90000, 120000));
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


}
