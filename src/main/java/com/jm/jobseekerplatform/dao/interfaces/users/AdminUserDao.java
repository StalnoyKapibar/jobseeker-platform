package com.jm.jobseekerplatform.dao.interfaces.users;

import com.jm.jobseekerplatform.model.users.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserDao extends JpaRepository<AdminUser, Long> {

    @Query("select a from AdminUser a where a.profile.id =: adminProfileId")
    AdminUser findByProfileId(@Param("adminProfileId") Long adminProfileId);
}
