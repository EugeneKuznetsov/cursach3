package com.cardealer.controller;

import com.cardealer.controller.main.Main;
import com.cardealer.model.Product;
import com.cardealer.model.Users;
import com.cardealer.model.enums.ProductAppStatus;
import com.cardealer.model.enums.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/stats")
public class StatsCont extends Main {
    @GetMapping
    public String stats(Model model) {
        getCurrentUserAndRole(model);

        ProductAppStatus[] statuses = ProductAppStatus.values();
        String[] statusesNames = new String[statuses.length];
        int[] statusesValues = new int[statuses.length];

        for (int i = 0; i < statuses.length; i++) {
            statusesNames[i] = statuses[i].getName();
            statusesValues[i] = productAppRepo.findAllByStatus(statuses[i]).size();
        }

        model.addAttribute("statusesNames", statusesNames);
        model.addAttribute("statusesValues", statusesValues);

        List<Product> products = productRepo.findAll();
        products.sort(Comparator.comparing(Product::getName));
        Collections.reverse(products);

        String[] productsNames = new String[5];
        Arrays.fill(productsNames, "");
        float[] productsValues = new float[5];

        for (int i = 0; i < products.size(); i++) {
            if (i == 5) break;
            productsNames[i] = products.get(i).getName();
            productsValues[i] = products.get(i).getConversion();
        }

        model.addAttribute("productsNames", productsNames);
        model.addAttribute("productsValues", productsValues);

        List<Users> users = userRepo.findAllByRole(Role.USER);
        users.sort(Comparator.comparing(Users::getAppsSize));
        Collections.reverse(users);

        String[] usersNames = new String[5];
        Arrays.fill(usersNames, "");
        int[] usersValues = new int[5];

        for (int i = 0; i < users.size(); i++) {
            if (i == 5) break;
            usersNames[i] = users.get(i).getFio();
            usersValues[i] = users.get(i).getAppsSize();
        }

        model.addAttribute("usersNames", usersNames);
        model.addAttribute("usersValues", usersValues);

        return "stats";
    }
}
