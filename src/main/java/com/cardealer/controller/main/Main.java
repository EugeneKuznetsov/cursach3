package com.cardealer.controller.main;

import com.cardealer.model.Users;
import com.cardealer.model.enums.ProductAppStatus;
import com.cardealer.model.enums.ProductCategory;
import com.cardealer.model.enums.ProductEngine;
import com.cardealer.repo.ProductAppRepo;
import com.cardealer.repo.ProductRepo;
import com.cardealer.repo.UserNoteRepo;
import com.cardealer.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Main {
    @Autowired
    protected UserRepo userRepo;
    @Autowired
    protected ProductRepo productRepo;
    @Autowired
    protected ProductAppRepo productAppRepo;
    @Autowired
    protected UserNoteRepo userNoteRepo;

    @Value("${upload.img}")
    protected String uploadImg;

    protected void getCurrentUserAndRole(Model model) {
        model.addAttribute("role", getRole());
        model.addAttribute("user", getUser());

        model.addAttribute("productCategories", ProductCategory.values());
        model.addAttribute("productEngines", ProductEngine.values());
        model.addAttribute("productAppStatuses", ProductAppStatus.values());
    }

    protected Users getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((!(auth instanceof AnonymousAuthenticationToken)) && auth != null) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            return userRepo.findByUsername(userDetail.getUsername());
        }
        return null;
    }

    protected String getRole() {
        Users users = getUser();
        if (users == null) return "NOT";
        return users.getRole().toString();
    }

    public static String getDateNow() {
        return LocalDateTime.now().toString().substring(0, 10);
    }

    public static float round(float value) {
        long factor = (long) Math.pow(10, 2);
        value = value * factor;
        long tmp = Math.round(value);
        return (float) tmp / factor;
    }

    protected String saveFile(MultipartFile photo, String path) throws IOException {
        if (photo != null && !Objects.requireNonNull(photo.getOriginalFilename()).isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            File uploadDir = new File(uploadImg);
            if (!uploadDir.exists()) uploadDir.mkdir();
            String result = path + "/" + uuidFile + "_" + photo.getOriginalFilename();
            photo.transferTo(new File(uploadImg + "/" + result));
            return result;
        } else throw new IOException();
    }
}