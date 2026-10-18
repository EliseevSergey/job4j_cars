package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.model.Model;
import ru.job4j.repository.ModelRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleModelService implements ModelService {
    private final ModelRepository modelRepository;

    @Override
    public List<Model> findAll() {
        return modelRepository.findAll();
    }

    @Override
    public Optional<Model> findById(Integer modelId) {
        return modelRepository.findById(modelId);
    }

    @Override
    public List<Model> findByBrandId(Integer brandId) {
        return modelRepository.findByBrandId(brandId);
    }
}
