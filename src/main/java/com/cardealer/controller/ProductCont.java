package com.cardealer.controller;

import com.cardealer.controller.main.Main;
import com.cardealer.model.Product;
import com.cardealer.model.ProductApp;
import com.cardealer.model.enums.ProductCategory;
import com.cardealer.model.enums.ProductEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/products")
public class ProductCont extends Main {

    @Autowired
    private EmailCont emailCont;

    @GetMapping
    public String products(Model model) {
        getCurrentUserAndRole(model);
        model.addAttribute("products", productRepo.findAll());
        return "products";
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam String name) {
        getCurrentUserAndRole(model);
        model.addAttribute("name", name);
        model.addAttribute("products", productRepo.findAllByNameContaining(name));
        return "products";
    }

    @GetMapping("/{id}")
    public String product(Model model, @PathVariable Long id) {
        getCurrentUserAndRole(model);
        Product product = productRepo.getReferenceById(id);
        product.setCount(product.getCount() + 1);
        productRepo.save(product);
        model.addAttribute("product", product);
        return "product";
    }

    @PostMapping("/{id}/app")     //ОБРАБОТКА КНОПКИ ЗАКАЗАТЬ
    public String app(@PathVariable Long id, @RequestParam int count) {
        productAppRepo.save(new ProductApp(count, productRepo.getReferenceById(id), getUser()));

        // Отправка письма с подтверждением заказа
        String userEmail = getUser().getEmail();
        emailCont.sendOrderConfirmationEmail(userEmail);

        return "redirect:/products/{id}";
    }

    @GetMapping("/add")
    public String add(Model model) {
        getCurrentUserAndRole(model);

        return "product_add";
    }

    @PostMapping("/add")
    public String add(
            Model model, @RequestParam String name, @RequestParam int mileage, @RequestParam int weight,
            @RequestParam int power, @RequestParam int warranty, @RequestParam int price, @RequestParam int year,
            @RequestParam String origin, @RequestParam String description, @RequestParam ProductCategory category,
            @RequestParam ProductEngine engine, @RequestParam MultipartFile file, @RequestParam MultipartFile[] photos
    ) {
        Product product = new Product(name, mileage, weight, power, warranty, price, year, origin, description, category, engine);

        try {
            if (file != null && !file.isEmpty()) {
                product.setFile(saveFile(file, "products"));
            }

            if (photos != null && !photos[0].isEmpty()) {
                String[] res = new String[photos.length];
                for (int i = 0; i < photos.length; i++) {
                    res[i] = saveFile(photos[i], "products");
                }
                product.setPhotos(res);
            }

        } catch (IOException e) {
            model.addAttribute("message", "Некорректный ввод!");
            getCurrentUserAndRole(model);
            return "product_add";
        }

        product = productRepo.save(product);

        return "redirect:/products/" + product.getId();
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable Long id) {
        getCurrentUserAndRole(model);
        model.addAttribute("product", productRepo.getReferenceById(id));
        return "product_edit";
    }

    @PostMapping("/{id}/edit")
    public String edit(
            Model model, @RequestParam String name, @RequestParam int mileage, @RequestParam int weight,
            @RequestParam int power, @RequestParam int warranty, @RequestParam int price, @RequestParam int year,
            @RequestParam String origin, @RequestParam String description, @RequestParam ProductCategory category,
            @RequestParam ProductEngine engine, @RequestParam MultipartFile file, @RequestParam MultipartFile[] photos,
            @PathVariable Long id
    ) {
        Product product = productRepo.getReferenceById(id);

        product.set(name, mileage, weight, power, warranty, price, year, origin, description, category, engine);

        try {
            if (file != null && !file.isEmpty()) {
                product.setFile(saveFile(file, "products"));
            }

            if (photos != null && !photos[0].isEmpty()) {
                String[] res = new String[photos.length];
                for (int i = 0; i < photos.length; i++) {
                    res[i] = saveFile(photos[i], "products");
                }
                product.setPhotos(res);
            }

        } catch (IOException e) {
            model.addAttribute("message", "Некорректный ввод!");
            getCurrentUserAndRole(model);
            model.addAttribute("product", productRepo.getReferenceById(id));
            return "product_edit";
        }

        productRepo.save(product);

        return "redirect:/products/{id}";
    }


    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        productRepo.deleteById(id);
        return "redirect:/products";
    }
}
