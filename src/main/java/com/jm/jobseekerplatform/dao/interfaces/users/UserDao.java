package com.jm.jobseekerplatform.dao.interfaces.users;

import com.jm.jobseekerplatform.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    User findByEmail(String email);

    boolean isExistEmail(String email);

}
