package ru.job4j.service;

import ru.job4j.model.Model;

import java.util.List;
import java.util.Optional;

public interface ModelService {
    List<Model> findAll();
    Optional<Model> findById(Integer modelId);

    List<Model> findByBrandId(Integer brandId);
}
