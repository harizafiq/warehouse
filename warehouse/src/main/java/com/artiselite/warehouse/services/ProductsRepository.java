package com.artiselite.warehouse.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.artiselite.warehouse.models.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer>{

}
