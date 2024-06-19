package com.cardealer.model;

import com.cardealer.model.enums.ProductAppStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ProductApp implements Serializable {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    private int count;

    @Enumerated(EnumType.STRING)
    private ProductAppStatus status = ProductAppStatus.WAITING;

    @ManyToOne
    private Product product;
    @ManyToOne
    private Users owner;

    public ProductApp(int count, Product product, Users owner) {
        this.count = count;
        this.product = product;
        this.owner = owner;
    }

    public int getPrice() {
        return product.getPrice() * count;
    }
}