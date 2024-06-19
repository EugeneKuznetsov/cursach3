package com.cardealer.repo;

import com.cardealer.model.ProductApp;
import com.cardealer.model.enums.ProductAppStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductAppRepo extends JpaRepository<ProductApp, Long> {
    List<ProductApp> findAllByStatus(ProductAppStatus status);
}