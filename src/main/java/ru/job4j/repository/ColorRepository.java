package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Color;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ColorRepository {
    private final CrudRepository crudRepository;

    public List<Color> findAll() {
        return crudRepository.query("FROM Color c ORDER BY c.name", Color.class);
    }

    public Optional<Color> findById(Integer colorId) {
        return crudRepository.optional("FROM Color c WHERE c.id = :fId", Color.class, Map.of("fId", colorId));
    }
}
