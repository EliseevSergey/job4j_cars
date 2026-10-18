package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.model.Brand;
import ru.job4j.repository.BrandRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleBrandService implements BrandService {
    private final BrandRepository brandRepository;

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public Optional<Brand> findById(Integer brandId) {
        return brandRepository.findById(brandId);
    }


}
