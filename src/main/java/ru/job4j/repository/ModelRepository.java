package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ModelRepository {
    private final CrudRepository crudRepository;

    public List<Model> findAll() {
        return crudRepository.query("FROM Model m JOIN FETCH m.brand ORDER BY m.name", Model.class);
    }

    public Optional<Model> findById(Integer modelId) {
        return crudRepository.optional("FROM Model m JOIN FETCH m.brand WHERE m.id = :fId",
                Model.class, Map.of("fId", modelId));
    }

    public List<Model> findByBrandId(Integer brandId) {
        return crudRepository.query("FROM Model m JOIN FETCH m.brand WHERE m.brand.id = :fId ORDER BY m.name",
                Model.class, Map.of("fId", brandId));
    }
}
