package com.cardealer.controller;

import com.cardealer.controller.main.Main;
import com.cardealer.model.ProductApp;
import com.cardealer.model.enums.ProductAppStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/apps")
public class ProductAppCont extends Main {
    @GetMapping
    public String apps(Model model) {
        getCurrentUserAndRole(model);
        model.addAttribute("apps", getUser().getApps());
        return "apps";
    }

    @GetMapping("/{status}")
    public String apps(Model model, @PathVariable ProductAppStatus status) {
        getCurrentUserAndRole(model);
        model.addAttribute("apps", productAppRepo.findAllByStatus(status));
        return "apps";
    }

    @GetMapping("/{id}/accepted")
    public String accepted(@PathVariable Long id) {
        ProductApp app = productAppRepo.getReferenceById(id);
        app.setStatus(ProductAppStatus.ACCEPTED);
        productAppRepo.save(app);
        return "redirect:/apps/WAITING";
    }

    @GetMapping("/{id}/done")
    public String done(@PathVariable Long id) {
        ProductApp app = productAppRepo.getReferenceById(id);
        app.setStatus(ProductAppStatus.DONE);
        productAppRepo.save(app);
        return "redirect:/apps/ACCEPTED";
    }
}