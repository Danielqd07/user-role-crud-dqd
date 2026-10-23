package org.iesalixar.daw2.danielquintero.ud3_user_role_crud_dqd.daos;

import org.iesalixar.daw2.danielquintero.ud3_user_role_crud_dqd.entities.User;

import java.util.List;

public interface UserDAO {

    List<User> listAllUsers();

    User getUserById(Long id);

    void insertUser(User user);

    void updateUser(User user);

    void deleteUser(Long id);

    boolean existsUserByUsername(String username);

    boolean existsUserByUsernameAndNotId(
            String username,
            Long id
    );

    boolean existsUserByEmail(String email);

    boolean existsUserByEmailAndNotId(
            String email,
            Long id
    );
}