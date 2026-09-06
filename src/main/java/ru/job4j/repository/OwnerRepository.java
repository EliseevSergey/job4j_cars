package ru.job4j.repository;

import lombok.AllArgsConstructor;
import ru.job4j.model.Owner;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class OwnerRepository {
    private final CrudRepository crudRepository;

    public Owner create(Owner newOwner) {
        crudRepository.run(session -> session.persist(newOwner));
        return newOwner;
    }

    public boolean delete(Integer ownerId) {
        return crudRepository.execute("DELETE FROM Owner WHERE id = :fId",
                Map.of("fId", ownerId)) > 0;
    }

    public Optional<Owner> findById(Integer ownerId) {
        return crudRepository.optional("FROM Owner WHERE id = :fId",
                Owner.class, Map.of("fId", ownerId));
    }

    public List<Owner> findByLikeName(String key) {
        return crudRepository.query("FROM Owner WHERE LOWER(name) LIKE LOWER(:fKey)", Owner.class,
                Map.of("fKey", "%" + key + "%")
        );
    }

    public List<Owner> findAll() {
        return crudRepository.query("FROM Owner ORDER BY id ASC", Owner.class);
    }
}
