package org.iesalixar.daw2.danielquintero.ud3_user_role_crud_dqd.entities;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    private Long id;

    @NotEmpty(message = "{msg.role.name.notEmpty}")
    @Size(max = 100, message = "{msg.role.name.size}")
    private String name;

    public Role(String name) {
        this.name = name;
    }
}