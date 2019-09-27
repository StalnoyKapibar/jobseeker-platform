package com.jm.jobseekerplatform.dao.impl.users;

import com.jm.jobseekerplatform.dao.AbstractDAO;
import com.jm.jobseekerplatform.model.users.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userDAO")
public class UserDAO extends AbstractDAO<User> {

    public User findByEmail(String email) {
        return entityManager
                .createQuery("SELECT u FROM User u WHERE u.email = :param", User.class)
                .setParameter("param", email)
                .getSingleResult();
    }

    public boolean isExistEmail(String email) {
        return (boolean) entityManager
                .createQuery("SELECT CASE WHEN EXISTS (SELECT r FROM User r WHERE r.email = :param) THEN true ELSE false END FROM User")
                .setParameter("param", email)
                .getSingleResult();
    }

    /**
     * Разблокируем пользователей, у которых закончился период блокировки
     */
    public void unblockBlockedUsersWithExpiryBanDate() {
        String query = "UPDATE profile pf " +
                "LEFT JOIN users u ON pf.id = u.profile_id " +
                "SET pf.expiry_block = NULL, pf.state = 'ACCESS', u.enabled = 1 " +
                "WHERE pf.expiry_block IS NOT NULL AND pf.expiry_block < NOW()";
        entityManager
                .createNativeQuery(query)
                .executeUpdate();
    }

    /**
     * Удаление пользователей, которые заблокированы бессроччно
     */
    public void deletePermanentBlockUsers() {
        String query = "SELECT u FROM User u LEFT JOIN EmployerProfile ep" +
                " ON u.profile.id = ep.id  WHERE ep.state = 'BLOCK_PERMANENT'";
        List<User> users = entityManager
                .createQuery(query, User.class)
                .getResultList();
        users.forEach(this::delete);
    }

    /**
     * Удаление User и всех связаных записей с помощью хранимой процедуры в БД
     * Необходимо поддерживать актуальность, при изменении схемы БД
     *
     * Если процедура отсутствует в БД, необходимо запустить:
     *  - StoredProcedureDAO.createDeleteUserProcedure()
     *  либо полностью пересоздать БД, с помощью Init
     *
     * @param user
     */
    @Override
    public void delete(User user){
        entityManager
                .createNativeQuery("CALL deleteUser(:profileId, :userId)")
                .setParameter("profileId", user.getProfile().getId())
                .setParameter("userId", user.getId())
                .executeUpdate();
    }
}
