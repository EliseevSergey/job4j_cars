package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Brand;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class BrandRepository {
    private final CrudRepository crudRepository;

    public List<Brand> findAll() {
       return crudRepository.query("FROM Brand b ORDER BY b.name", Brand.class);
    }

    public Optional<Brand> findById(Integer brandId) {
        return crudRepository.optional("FROM Brand b WHERE b.id = :fId", Brand.class, Map.of("fId", brandId));
    }
}
