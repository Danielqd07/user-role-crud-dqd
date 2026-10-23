package org.iesalixar.daw2.danielquintero.ud3_user_role_crud_dqd.daos;

import org.iesalixar.daw2.danielquintero.ud3_user_role_crud_dqd.entities.Role;
import org.iesalixar.daw2.danielquintero.ud3_user_role_crud_dqd.entities.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(UserDAOImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public UserDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userRowMapper =
            (rs, rowNum) -> {

                User user = new User();

                user.setId(
                        rs.getLong("id")
                );

                user.setUsername(
                        rs.getString("username")
                );

                user.setEmail(
                        rs.getString("email")
                );

                user.setPassword(
                        rs.getString("password")
                );

                return user;
            };

    @Override
    public List<User> listAllUsers() {

        String sql = """
                SELECT id, username, email, password
                FROM users
                ORDER BY id
                """;

        List<User> users =
                jdbcTemplate.query(
                        sql,
                        userRowMapper
                );

        for (User user : users) {

            user.setRoles(
                    getRolesByUserId(
                            user.getId()
                    )
            );
        }

        logger.info(
                "Retrieved {} users from the database",
                users.size()
        );

        return users;
    }

    @Override
    public User getUserById(Long id) {

        String sql = """
                SELECT id, username, email, password
                FROM users
                WHERE id = ?
                """;

        List<User> users =
                jdbcTemplate.query(
                        sql,
                        userRowMapper,
                        id
                );

        if (!users.isEmpty()) {

            User user =
                    users.getFirst();

            user.setRoles(
                    getRolesByUserId(id)
            );

            return user;
        }

        return null;
    }

    @Override
    public void insertUser(User user) {

        String sql = """
                INSERT INTO users
                (username, email, password)
                VALUES (?, ?, ?)
                """;

        KeyHolder keyHolder =
                new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {

                    PreparedStatement ps =
                            connection.prepareStatement(
                                    sql,
                                    Statement.RETURN_GENERATED_KEYS
                            );

                    ps.setString(
                            1,
                            user.getUsername()
                    );

                    ps.setString(
                            2,
                            user.getEmail()
                    );

                    ps.setString(
                            3,
                            user.getPassword()
                    );

                    return ps;

                },
                keyHolder
        );

        Long userId =
                keyHolder.getKey().longValue();

        insertUserRoles(
                userId,
                user.getRoles()
        );

        logger.info(
                "Inserted user {} with id {}",
                user.getUsername(),
                userId
        );
    }

    @Override
    public void updateUser(User user) {

        String sql = """
                UPDATE users
                SET username = ?,
                    email = ?,
                    password = ?
                WHERE id = ?
                """;

        jdbcTemplate.update(
                sql,
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getId()
        );

        deleteUserRoles(
                user.getId()
        );

        insertUserRoles(
                user.getId(),
                user.getRoles()
        );

        logger.info(
                "Updated user with id {}",
                user.getId()
        );
    }

    @Override
    public void deleteUser(Long id) {

        String sql = """
                DELETE FROM users
                WHERE id = ?
                """;

        int rowsAffected =
                jdbcTemplate.update(
                        sql,
                        id
                );

        logger.info(
                "Deleted user. Rows affected: {}",
                rowsAffected
        );
    }

    @Override
    public boolean existsUserByUsername(
            String username) {

        String sql = """
                SELECT COUNT(*)
                FROM users
                WHERE UPPER(username) = UPPER(?)
                """;

        Integer count =
                jdbcTemplate.queryForObject(
                        sql,
                        Integer.class,
                        username
                );

        return count != null && count > 0;
    }

    @Override
    public boolean existsUserByUsernameAndNotId(
            String username,
            Long id) {

        String sql = """
                SELECT COUNT(*)
                FROM users
                WHERE UPPER(username) = UPPER(?)
                  AND id != ?
                """;

        Integer count =
                jdbcTemplate.queryForObject(
                        sql,
                        Integer.class,
                        username,
                        id
                );

        return count != null && count > 0;
    }

    @Override
    public boolean existsUserByEmail(
            String email) {

        String sql = """
                SELECT COUNT(*)
                FROM users
                WHERE UPPER(email) = UPPER(?)
                """;

        Integer count =
                jdbcTemplate.queryForObject(
                        sql,
                        Integer.class,
                        email
                );

        return count != null && count > 0;
    }

    @Override
    public boolean existsUserByEmailAndNotId(
            String email,
            Long id) {

        String sql = """
                SELECT COUNT(*)
                FROM users
                WHERE UPPER(email) = UPPER(?)
                  AND id != ?
                """;

        Integer count =
                jdbcTemplate.queryForObject(
                        sql,
                        Integer.class,
                        email,
                        id
                );

        return count != null && count > 0;
    }

    private List<Role> getRolesByUserId(
            Long userId) {

        String sql = """
                SELECT r.id, r.name
                FROM roles r
                INNER JOIN user_roles ur
                    ON r.id = ur.role_id
                WHERE ur.user_id = ?
                ORDER BY r.id
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {

                    Role role = new Role();

                    role.setId(
                            rs.getLong("id")
                    );

                    role.setName(
                            rs.getString("name")
                    );

                    return role;
                },
                userId
        );
    }

    private void insertUserRoles(
            Long userId,
            List<Role> roles) {

        if (roles == null) {
            return;
        }

        for (Role role : roles) {

            String sql = """
                    INSERT INTO user_roles
                    (user_id, role_id)
                    VALUES (?, ?)
                    """;

            jdbcTemplate.update(
                    sql,
                    userId,
                    role.getId()
            );
        }
    }

    private void deleteUserRoles(
            Long userId) {

        String sql = """
                DELETE FROM user_roles
                WHERE user_id = ?
                """;

        jdbcTemplate.update(
                sql,
                userId
        );
    }
}