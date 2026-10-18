package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Engine;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class EngineRepository {
    private final CrudRepository crudRepository;

    public Engine create(Engine engine) {
        crudRepository.run(session -> session.persist(engine));
        return engine;
    }

    public Optional<Engine> findById(Integer engineId) {
        return crudRepository.optional("FROM Engine e JOIN FETCH e.fuel WHERE e.id = :fId",
                Engine.class, Map.of("fId", engineId));
    }

    public List<Engine> findAll() {
        return crudRepository.query("FROM Engine e JOIN FETCH e.fuel ORDER BY e.id ASC", Engine.class);
    }


    public boolean delete(Integer engineId) {
        return crudRepository.execute("DELETE FROM Engine e WHERE e.id = :fId",
                Map.of("fId", engineId)) > 0;
    }

    @Deprecated
    public List<Engine> findByLikeName(String key) {
        return crudRepository.query("FROM Engine WHERE LOWER(name) LIKE LOWER(:fKey)", Engine.class,
                Map.of("fKey", "%" + key + "%")
        );
    }

    /*public boolean update(Engine engine) {
        return crudRepository.execute("UPDATE Engine e SET "
                        + "e.fuel = :fFuel, "
                        + "e.volume = :fVolume, "
                        + "e.horsePower = :fHorsePower "
                        + "WHERE e.id = :fId",
                Map.of("fId", engine.getId(),
                        "fFuel", engine.getFuel(),
                        "fVolume", engine.getVolume(),
                        "fHorsePower", engine.getHorsePower())) > 0;
    }*/

    public Engine update(Engine engine) {
        crudRepository.tx(session -> session.merge(engine));
        return engine;
    }
}
