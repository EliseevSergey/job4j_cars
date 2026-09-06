package ru.job4j.repository;

import lombok.AllArgsConstructor;
import ru.job4j.model.Engine;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class EngineRepository {
    private final CrudRepository crudRepository;

    public Engine create(Engine engine) {
        crudRepository.run(session -> session.persist(engine));
        return engine;
    }

    public Optional<Engine> findById(Integer engineId) {
        return crudRepository.optional("FROM Engine WHERE id = :fId",
                Engine.class, Map.of("fId", engineId));
    }

    public List<Engine> findAll() {
        return crudRepository.query("FROM Engine ORDER BY id ASC", Engine.class);
    }

    public boolean updateName(Integer engineId, String newName) {
        return crudRepository.execute("UPDATE Engine SET name = :newName WHERE id = :fId",
                Map.of("fId", engineId, "newName", newName)) > 0;
    }

    public boolean delete(Integer engineId) {
        return crudRepository.execute("DELETE FROM Engine WHERE id = :fId",
                Map.of("fId", engineId)) > 0;
    }

    public List<Engine> findByLikeName(String key) {
        return crudRepository.query("FROM Engine WHERE LOWER(name) LIKE LOWER(:fKey)", Engine.class,
                Map.of("fKey", "%" + key + "%")
        );
    }
}
