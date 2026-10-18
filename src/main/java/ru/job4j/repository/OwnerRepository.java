package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Owner;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class OwnerRepository {
    private final CrudRepository crudRepository;

    public Owner create(Owner newOwner) {
        crudRepository.run(session -> session.persist(newOwner));
        return newOwner;
    }

    public boolean delete(Integer ownerId) {
        return crudRepository.execute("DELETE FROM Owner o WHERE o.id = :fId",
                Map.of("fId", ownerId)) > 0;
    }

    public Optional<Owner> findById(Integer ownerId) {
        return crudRepository.optional("FROM Owner o WHERE o.id = :fId",
                Owner.class, Map.of("fId", ownerId));
    }

    public List<Owner> findByLikeName(String key) {
        return crudRepository.query("FROM Owner o WHERE LOWER(o.name) LIKE LOWER(:fKey)", Owner.class,
                Map.of("fKey", "%" + key + "%")
        );
    }

    public List<Owner> findAll() {
        return crudRepository.query("FROM Owner o ORDER BY o.id ASC", Owner.class);
    }
}
