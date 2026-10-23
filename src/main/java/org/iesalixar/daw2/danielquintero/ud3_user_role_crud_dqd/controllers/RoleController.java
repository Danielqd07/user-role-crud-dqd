package org.iesalixar.daw2.danielquintero.ud3_user_role_crud_dqd.controllers;

import jakarta.validation.Valid;

import org.iesalixar.daw2.danielquintero.ud3_user_role_crud_dqd.daos.RoleDAO;
import org.iesalixar.daw2.danielquintero.ud3_user_role_crud_dqd.entities.Role;

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

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String listRoles(Model model) {

        List<Role> listRoles =
                roleDAO.listAllRoles();

        model.addAttribute(
                "listRoles",
                listRoles
        );

        return "views/role/role-list";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {

        model.addAttribute(
                "role",
                new Role()
        );

        return "views/role/role-form";
    }

    @PostMapping("/insert")
    public String insertRole(
            @Valid @ModelAttribute("role") Role role,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        if (result.hasErrors()) {
            return "views/role/role-form";
        }

        if (roleDAO.existsRoleByName(
                role.getName())) {

            String errorMessage =
                    messageSource.getMessage(
                            "msg.role-controller.insert.nameExist",
                            null,
                            locale
                    );

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    errorMessage
            );

            return "redirect:/roles/new";
        }

        roleDAO.insertRole(role);

        return "redirect:/roles";
    }

    @GetMapping("/edit")
    public String showEditForm(
            @RequestParam("id") Long id,
            Model model) {

        Role role =
                roleDAO.getRoleById(id);

        model.addAttribute(
                "role",
                role
        );

        return "views/role/role-form";
    }

    @PostMapping("/update")
    public String updateRole(
            @Valid @ModelAttribute("role") Role role,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        if (result.hasErrors()) {
            return "views/role/role-form";
        }

        if (roleDAO.existsRoleByNameAndNotId(
                role.getName(),
                role.getId())) {

            String errorMessage =
                    messageSource.getMessage(
                            "msg.role-controller.update.nameExist",
                            null,
                            locale
                    );

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    errorMessage
            );

            return "redirect:/roles/edit?id="
                    + role.getId();
        }

        roleDAO.updateRole(role);

        return "redirect:/roles";
    }

    @PostMapping("/delete")
    public String deleteRole(
            @RequestParam("id") Long id) {

        roleDAO.deleteRole(id);

        return "redirect:/roles";
    }
}