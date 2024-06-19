package com.cardealer.controller;

import com.cardealer.controller.main.Main;
import com.cardealer.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/profile")
public class ProfileCont extends Main {

    @Autowired
    private EmailRegCont emailCont;
    
    @GetMapping
    public String profile(Model model) {
        getCurrentUserAndRole(model);
        return "profile";
    }

    @PostMapping("/edit")
    public String edit(@RequestParam String fio, @RequestParam int age, @RequestParam String tel, @RequestParam String email) {
        Users user = getUser();
        user.set(fio, age, tel, email);
        userRepo.save(user);

        // Отправка письма с подтверждением регистрации
        emailCont.sendRegistrationConfirmationEmail(email);

        return "redirect:/profile";
    }

    @PostMapping("/photo")
    public String photo(Model model, @RequestParam MultipartFile photo) {
        Users user = getUser();

        try {
            if (photo != null && !Objects.requireNonNull(photo.getOriginalFilename()).isEmpty()) {
                user.setPhoto(saveFile(photo, "users"));
            }
        } catch (IOException e) {
            model.addAttribute("message", "Некорректные данные!");
            getCurrentUserAndRole(model);
            return "profile";
        }

        userRepo.save(user);
        return "redirect:/profile";
    }

}
