package org.iesalixar.daw2.danielquintero.ud3_user_role_crud_dqd.entities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    @NotEmpty(message = "{msg.user.username.notEmpty}")
    @Size(max = 100, message = "{msg.user.username.size}")
    private String username;

    @NotEmpty(message = "{msg.user.email.notEmpty}")
    @Email(message = "{msg.user.email.valid}")
    @Size(max = 150, message = "{msg.user.email.size}")
    private String email;

    @NotEmpty(message = "{msg.user.password.notEmpty}")
    @Size(max = 255, message = "{msg.user.password.size}")
    private String password;

    private List<Role> roles = new ArrayList<>();

    public User(
            String username,
            String email,
            String password) {

        this.username = username;
        this.email = email;
        this.password = password;
    }
}