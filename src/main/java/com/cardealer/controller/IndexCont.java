package com.cardealer.controller;

import com.cardealer.controller.main.Main;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexCont extends Main {
    @GetMapping
    public String index1(Model model) {
        getCurrentUserAndRole(model);
        return "index";
    }

    @GetMapping("/index")
    public String index2(Model model) {
        getCurrentUserAndRole(model);
        return "index";
    }

    @GetMapping("/undefined")
    public String undefined() {
        return "redirect:/index";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        getCurrentUserAndRole(model);
        return "contact";
    }

    @GetMapping("/about")
    public String about(Model model) {
        getCurrentUserAndRole(model);
        return "about";
    }
}