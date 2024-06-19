package com.cardealer.model;

import com.cardealer.controller.main.Main;
import com.cardealer.model.enums.ProductCategory;
import com.cardealer.model.enums.ProductEngine;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product implements Serializable {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    private String name;
    private String[] photos;
    private int mileage;
    private int weight;
    private int power;
    private int warranty;
    private int price;
    private String file;
    private int year;
    private String origin;
    private int count = 0;

    @Column(length = 5000)
    private String description;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;
    @Enumerated(EnumType.STRING)
    private ProductEngine engine;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductApp> apps = new ArrayList<>();

    public Product(String name, int mileage, int weight, int power, int warranty, int price, int year, String origin, String description, ProductCategory category, ProductEngine engine) {
        this.name = name;
        this.mileage = mileage;
        this.weight = weight;
        this.power = power;
        this.warranty = warranty;
        this.price = price;
        this.year = year;
        this.origin = origin;
        this.description = description;
        this.category = category;
        this.engine = engine;
    }

    public void set(String name, int mileage, int weight, int power, int warranty, int price, int year, String origin, String description, ProductCategory category, ProductEngine engine) {
        this.name = name;
        this.mileage = mileage;
        this.weight = weight;
        this.power = power;
        this.warranty = warranty;
        this.price = price;
        this.year = year;
        this.origin = origin;
        this.description = description;
        this.category = category;
        this.engine = engine;
    }

    public float getConversion() {
        if (apps.isEmpty()) return 0;
        return Main.round((float) apps.size() / count * 100);
    }

}
