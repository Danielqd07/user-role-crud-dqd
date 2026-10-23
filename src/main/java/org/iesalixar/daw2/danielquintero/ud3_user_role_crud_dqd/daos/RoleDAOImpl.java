package org.iesalixar.daw2.danielquintero.ud3_user_role_crud_dqd.daos;

import org.iesalixar.daw2.danielquintero.ud3_user_role_crud_dqd.entities.Role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleDAOImpl implements RoleDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(RoleDAOImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public RoleDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Role> roleRowMapper =
            (rs, rowNum) -> {

                Role role = new Role();

                role.setId(
                        rs.getLong("id")
                );

                role.setName(
                        rs.getString("name")
                );

                return role;
            };

    @Override
    public List<Role> listAllRoles() {

        String sql = """
                SELECT id, name
                FROM roles
                ORDER BY id
                """;

        List<Role> roles =
                jdbcTemplate.query(
                        sql,
                        roleRowMapper
                );

        logger.info(
                "Retrieved {} roles from the database",
                roles.size()
        );

        return roles;
    }

    @Override
    public Role getRoleById(Long id) {

        String sql = """
                SELECT id, name
                FROM roles
                WHERE id = ?
                """;

        List<Role> roles =
                jdbcTemplate.query(
                        sql,
                        roleRowMapper,
                        id
                );

        if (!roles.isEmpty()) {
            return roles.getFirst();
        }

        return null;
    }

    @Override
    public void insertRole(Role role) {

        String sql = """
                INSERT INTO roles (name)
                VALUES (?)
                """;

        int rowsAffected =
                jdbcTemplate.update(
                        sql,
                        role.getName()
                );

        logger.info(
                "Inserted role. Rows affected: {}",
                rowsAffected
        );
    }

    @Override
    public void updateRole(Role role) {

        String sql = """
                UPDATE roles
                SET name = ?
                WHERE id = ?
                """;

        int rowsAffected =
                jdbcTemplate.update(
                        sql,
                        role.getName(),
                        role.getId()
                );

        logger.info(
                "Updated role. Rows affected: {}",
                rowsAffected
        );
    }

    @Override
    public void deleteRole(Long id) {

        String sql = """
                DELETE FROM roles
                WHERE id = ?
                """;

        int rowsAffected =
                jdbcTemplate.update(
                        sql,
                        id
                );

        logger.info(
                "Deleted role. Rows affected: {}",
                rowsAffected
        );
    }

    @Override
    public boolean existsRoleByName(String name) {

        String sql = """
                SELECT COUNT(*)
                FROM roles
                WHERE UPPER(name) = UPPER(?)
                """;

        Integer count =
                jdbcTemplate.queryForObject(
                        sql,
                        Integer.class,
                        name
                );

        return count != null && count > 0;
    }

    @Override
    public boolean existsRoleByNameAndNotId(
            String name,
            Long id) {

        String sql = """
                SELECT COUNT(*)
                FROM roles
                WHERE UPPER(name) = UPPER(?)
                  AND id != ?
                """;

        Integer count =
                jdbcTemplate.queryForObject(
                        sql,
                        Integer.class,
                        name,
                        id
                );

        return count != null && count > 0;
    }
}