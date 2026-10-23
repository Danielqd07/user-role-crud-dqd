package org.iesalixar.daw2.danielquintero.ud3_user_role_crud_dqd.daos;

import org.iesalixar.daw2.danielquintero.ud3_user_role_crud_dqd.entities.Role;

import java.util.List;

public interface RoleDAO {

    List<Role> listAllRoles();

    Role getRoleById(Long id);

    void insertRole(Role role);

    void updateRole(Role role);

    void deleteRole(Long id);

    boolean existsRoleByName(String name);

    boolean existsRoleByNameAndNotId(
            String name,
            Long id
    );
}