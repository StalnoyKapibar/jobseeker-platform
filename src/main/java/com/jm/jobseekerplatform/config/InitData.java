package com.jm.jobseekerplatform.config;

import com.jm.jobseekerplatform.model.EmployerProfile;
import com.jm.jobseekerplatform.model.User;
import com.jm.jobseekerplatform.model.UserRole;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.service.impl.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.UserRoleService;
import com.jm.jobseekerplatform.service.impl.UserService;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
                "\n" +
                "Белая заработная плата, официальное трудоустройство\n" +
                "Гибкий график работы\n" +
                "Лояльное отношение к сотрудникам\n" +
                "Дружный коллектив\n" +
                "Дополнительная информация:\n" +
                "\n" +
                "Мы ищем талантливых специалистов! Если Вы уверены в себе и хотите заниматься любимым делом профессионально, пишите нам! Мы хотим видеть людей, готовых работать над серьезными проектами и добиваться отличных результатов. Мы предлагаем интересную работу в дружном и профессиональном коллективе, в котором ценится работа каждого. Вы можете стать частью нашей команды!", 110000, 140000));
        vacancyService.add(new Vacancy("Java Developer", "Санкт-Петербург", false,"Участвовать в проектировании сервисов, оптимизировать высоконагруженный проект, внедрять новые технологии и Big Data хранилищ. Участвовать в формулировании и декомпозиции продуктовых...\n" +
                "Имеете опыт работы со Spring Boot, Spring Data JPA, Rabbit MQ. Знаете и понимаете шаблоны проектирования, клиент-серверные технологии.", 90000, 120000));
    }

    public void initEmployerProfiles() {
        Set<Vacancy> vacancies = new HashSet<>();
        vacancies.add(vacancyService.getById(1L));
        vacancies.add(vacancyService.getById(2L));
        employerProfileService.add(new EmployerProfile("Рога и копыта", "www.roga.ru", "Мы - классная команда", new byte[]{1,2,3}, vacancies));

        vacancies.clear();
        vacancies.add(vacancyService.getById(3L));
        employerProfileService.add(new EmployerProfile("Вектор", "www.vector.ru", "Мы хотим ни много ни мало изменить микро-бизнес в России. Поэтому наша цель - создать качественное решение и показать предпринимателям, что их бизнес может больше!", new byte[]{1,2,3}, vacancies));

    }


}
