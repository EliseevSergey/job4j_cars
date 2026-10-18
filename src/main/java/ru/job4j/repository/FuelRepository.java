package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Fuel;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class FuelRepository {
    private final CrudRepository crudRepository;

    public List<Fuel> findAll() {
        return crudRepository.query("FROM Fuel f ORDER BY f.name", Fuel.class);
    }

    public Optional<Fuel> findById(Integer fuelId) {
        return crudRepository.optional("FROM Fuel f WHERE f.id = :fId", Fuel.class, Map.of("fId", fuelId));
    }
}
