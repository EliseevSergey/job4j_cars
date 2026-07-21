package ru.job4j.repository;

import lombok.AllArgsConstructor;
import ru.job4j.model.Car;
import ru.job4j.model.Engine;
import ru.job4j.model.Owner;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class CarRepository {
    private final CrudRepository crudRepository;

    public Car create(Car car) {
        crudRepository.run(session -> session.persist(car));
        return car;
    }

    public boolean updateName(Integer carId, String newName) {
        return crudRepository.execute("UPDATE Car SET name = :newName WHERE id = :fId",
                Map.of("newName", newName, "fId", carId)) > 0;
    }

    public boolean updateEngine(Integer carId, Engine newEngine) {
        return crudRepository.execute("UPDATE Car SET engine = :newEngine WHERE id = :fId",
                Map.of("newEngine", newEngine, "fId", carId)) > 0;
    }

    public boolean delete(Integer carId) {
        return crudRepository.execute("DELETE FROM Car WHERE id = :fId", Map.of("fId", carId)) > 0;
    }

    public List<Car> findByLikeName(String key) {
        return crudRepository.query("FROM Car WHERE LOWER(name) LIKE LOWER(:fKey)", Car.class,
                Map.of("fKey", "%" + key + "%")
        );
    }

    public List<Car> findAll() {
        return crudRepository.query("FROM Car ORDER BY id ASC", Car.class);
    }

    public boolean addOwner(Integer carId, Integer ownerId) {
        return crudRepository.tx(session -> {
            Car car = session.get(Car.class, carId);
            Owner owner = session.get(Owner.class, ownerId);
            if (car == null || owner == null) {
                return false;
            }
            return car.getOwners().add(owner);
        });
    }

    public boolean removeOwner(Integer carId, Integer ownerId) {
        return crudRepository.tx(session -> {
            Car car = session.get(Car.class, carId);
            Owner owner = session.get(Owner.class, ownerId);
            if (car == null || owner == null) {
                return false;
            }
            return car.getOwners().remove(owner);
        });
    }
}
