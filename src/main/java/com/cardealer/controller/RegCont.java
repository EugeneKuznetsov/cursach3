package com.cardealer.controller;

import com.cardealer.controller.main.Main;
import com.cardealer.model.Users;
import com.cardealer.model.enums.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/reg")
public class RegCont extends Main {

    @GetMapping
    public String reg(Model model) {
        getCurrentUserAndRole(model);
        return "reg";
    }

    @PostMapping
    public String regUser(Model model, @RequestParam String username, @RequestParam String password) {
        if (userRepo.findAll().isEmpty()) {
            userRepo.save(new Users(username, password, Role.ADMIN));
            return "redirect:/login";
        }

        if (userRepo.findByUsername(username) != null) {
            model.addAttribute("message", "Пользователь с таким логином уже существует");
            getCurrentUserAndRole(model);
            return "reg";
        }

        userRepo.save(new Users(username, password, Role.USER));

        return "redirect:/login";
    }
}
