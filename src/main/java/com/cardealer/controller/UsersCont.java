package com.cardealer.controller;

import com.cardealer.controller.main.Main;
import com.cardealer.model.UserNote;
import com.cardealer.model.Users;
import com.cardealer.model.enums.Role;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UsersCont extends Main {

    @Autowired
    private EmailRegCont emailCont;

    @GetMapping
    public String users(Model model) {

        getCurrentUserAndRole(model);

        List<Users> users;

        if (getUser().getRole() == Role.ADMIN) {
            users = userRepo.findAll();
        } else {
            users = userRepo.findAllByRole(Role.USER);
        }

        model.addAttribute("users", users);

        model.addAttribute("roles", Role.values());
        return "users";
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam String username) {
        getCurrentUserAndRole(model);
        model.addAttribute("username", username);

        List<Users> users;

        if (getUser().getRole() == Role.ADMIN) {
            users = userRepo.findAll();
        } else {
            users = userRepo.findAllByRole(Role.USER);
        }

        users = users.stream().filter(user -> user.getUsername().toLowerCase().contains(username)).toList();

        model.addAttribute("users", users);

        model.addAttribute("roles", Role.values());
        return "users";
    }

    @GetMapping("/{id}")
    public String user(Model model, @PathVariable Long id) {
        getCurrentUserAndRole(model);
        model.addAttribute("u", userRepo.getReferenceById(id));
        return "user";
    }

//    @PostMapping("/{id}/edit")
    @PostMapping(" /profile/edit")
    public String edit(@PathVariable Long id, @RequestParam String fio, @RequestParam int age, @RequestParam String tel, @RequestParam String email) {
        Users user = userRepo.getReferenceById(id);
        user.set(fio, age, tel, email);
        userRepo.save(user);



        return "redirect:/users/{id}";
    }

    @PostMapping("/{id}/note")
    public String note(@PathVariable Long id, @RequestParam String text) {
        userNoteRepo.save(new UserNote(text, getUser(), userRepo.getReferenceById(id)));
        return "redirect:/users/{id}";
    }

    @PostMapping("/{id}/role")
    public String role(@PathVariable Long id, @RequestParam Role role) {
        Users user = userRepo.getReferenceById(id);
        user.setRole(role);
        userRepo.save(user);
        return "redirect:/users";
    }

    @GetMapping("/{id}/delete")
    public String userDelete(@PathVariable Long id) {
        userRepo.deleteById(id);
        return "redirect:/users";
    }
}
