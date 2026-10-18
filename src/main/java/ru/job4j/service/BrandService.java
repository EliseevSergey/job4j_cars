package ru.job4j.service;

import ru.job4j.model.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandService {
    List<Brand> findAll();
    Optional<Brand> findById(Integer brandId);
}
