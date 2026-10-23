package org.iesalixar.daw2.danielquintero.ud3_user_role_crud_dqd.controllers;

import jakarta.validation.Valid;

import org.iesalixar.daw2.danielquintero.ud3_user_role_crud_dqd.daos.RoleDAO;
import org.iesalixar.daw2.danielquintero.ud3_user_role_crud_dqd.daos.UserDAO;
import org.iesalixar.daw2.danielquintero.ud3_user_role_crud_dqd.entities.Role;
import org.iesalixar.daw2.danielquintero.ud3_user_role_crud_dqd.entities.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String listUsers(Model model) {

        List<User> listUsers =
                userDAO.listAllUsers();

        model.addAttribute(
                "listUsers",
                listUsers
        );

        return "views/user/user-list";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {

        model.addAttribute(
                "user",
                new User()
        );

        model.addAttribute(
                "listRoles",
                roleDAO.listAllRoles()
        );

        return "views/user/user-form";
    }

    @PostMapping("/insert")
    public String insertUser(
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            @RequestParam(
                    value = "roleIds",
                    required = false)
            List<Long> roleIds,
            Model model,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        assignRolesToUser(
                user,
                roleIds
        );

        if (user.getRoles() == null
                || user.getRoles().isEmpty()) {

            result.rejectValue(
                    "roles",
                    "msg.user.roles.notEmpty"
            );
        }

        if (result.hasErrors()) {

            model.addAttribute(
                    "listRoles",
                    roleDAO.listAllRoles()
            );

            return "views/user/user-form";
        }

        if (userDAO.existsUserByUsername(
                user.getUsername())) {

            String errorMessage =
                    messageSource.getMessage(
                            "msg.user-controller.insert.usernameExist",
                            null,
                            locale
                    );

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    errorMessage
            );

            return "redirect:/users/new";
        }

        if (userDAO.existsUserByEmail(
                user.getEmail())) {

            String errorMessage =
                    messageSource.getMessage(
                            "msg.user-controller.insert.emailExist",
                            null,
                            locale
                    );

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    errorMessage
            );

            return "redirect:/users/new";
        }

        userDAO.insertUser(user);

        return "redirect:/users";
    }

    @GetMapping("/edit")
    public String showEditForm(
            @RequestParam("id") Long id,
            Model model) {

        User user =
                userDAO.getUserById(id);

        model.addAttribute(
                "user",
                user
        );

        model.addAttribute(
                "listRoles",
                roleDAO.listAllRoles()
        );

        return "views/user/user-form";
    }

    @PostMapping("/update")
    public String updateUser(
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            @RequestParam(
                    value = "roleIds",
                    required = false)
            List<Long> roleIds,
            Model model,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        assignRolesToUser(
                user,
                roleIds
        );

        if (user.getRoles() == null
                || user.getRoles().isEmpty()) {

            result.rejectValue(
                    "roles",
                    "msg.user.roles.notEmpty"
            );
        }

        if (result.hasErrors()) {

            model.addAttribute(
                    "listRoles",
                    roleDAO.listAllRoles()
            );

            return "views/user/user-form";
        }

        if (userDAO.existsUserByUsernameAndNotId(
                user.getUsername(),
                user.getId())) {

            String errorMessage =
                    messageSource.getMessage(
                            "msg.user-controller.update.usernameExist",
                            null,
                            locale
                    );

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    errorMessage
            );

            return "redirect:/users/edit?id="
                    + user.getId();
        }

        if (userDAO.existsUserByEmailAndNotId(
                user.getEmail(),
                user.getId())) {

            String errorMessage =
                    messageSource.getMessage(
                            "msg.user-controller.update.emailExist",
                            null,
                            locale
                    );

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    errorMessage
            );

            return "redirect:/users/edit?id="
                    + user.getId();
        }

        userDAO.updateUser(user);

        return "redirect:/users";
    }

    @PostMapping("/delete")
    public String deleteUser(
            @RequestParam("id") Long id) {

        userDAO.deleteUser(id);

        return "redirect:/users";
    }

    private void assignRolesToUser(
            User user,
            List<Long> roleIds) {

        List<Role> roles =
                new ArrayList<>();

        if (roleIds != null) {

            for (Long roleId : roleIds) {

                Role role =
                        roleDAO.getRoleById(
                                roleId
                        );

                if (role != null) {
                    roles.add(role);
                }
            }
        }

        user.setRoles(roles);
    }
}