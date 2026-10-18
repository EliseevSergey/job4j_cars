package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Car;
import ru.job4j.model.Owner;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CarRepository {
    private final CrudRepository crudRepository;

    public Car create(Car car) {
        crudRepository.run(session -> session.persist(car));
        return car;
    }

    public Optional<Car> findById(Integer carId) {
        return crudRepository.optional("FROM Car c "
                + "JOIN FETCH c.brand "
                + "JOIN FETCH c.model "
                + "JOIN FETCH c.engine e "
                + "JOIN FETCH c.color "
                + "JOIN FETCH e.fuel WHERE c.id = :fId", Car.class, Map.of("fId", carId));
    }

    /*public boolean update(Car car) {
        return crudRepository.execute("UPDATE Car c SET "
                + "c.vin = : fVin, "
                + "c.brand = :fBrand, "
                + "c.model = :fModel, "
                + "c.engine = :fEngine, "
                + "c.color = :fColor "
                + "WHERE c.id = :fId", Map.of(
                "fId", car.getId(),
                "fVin", car.getVin(),
                "fBrand", car.getBrand(),
                "fModel", car.getModel(),
                "fEngine", car.getEngine(),
                "fColor", car.getColor())
        ) > 0;
    }*/

    public Car update(Car car) {
        crudRepository.tx(session -> session.merge(car));
        return car;
    }

    public boolean delete(Integer carId) {
        return crudRepository.execute("DELETE FROM Car c WHERE c.id = :fId", Map.of("fId", carId)) > 0;
    }


    public List<Car> findByLikeModelName(String key) {
        return crudRepository.query("FROM Car c "
                        + "JOIN FETCH c.brand "
                        + "JOIN FETCH c.model m "
                        + "JOIN FETCH c.engine e "
                        + "JOIN FETCH e.fuel "
                        + "JOIN FETCH c.color "
                        + "WHERE LOWER(m.name) LIKE LOWER(:fKey) ORDER BY m.name",
                Car.class,
                Map.of("fKey", "%" + key + "%")
        );
    }

    public List<Car> findByLikeBrandName(String key) {
        return crudRepository.query("FROM Car c "
                        + "JOIN FETCH c.brand b "
                        + "JOIN FETCH c.model "
                        + "JOIN FETCH c.engine e "
                        + "JOIN FETCH e.fuel "
                        + "JOIN FETCH c.color "
                        + "WHERE LOWER(b.name) LIKE LOWER(:fKey) "
                        + "ORDER BY b.name", Car.class,
                Map.of("fKey", "%" + key + "%")
        );
    }

    public List<Car> findAll() {
        return crudRepository.query("FROM Car c "
                + "JOIN FETCH c.brand "
                + "JOIN FETCH c.model "
                + "JOIN FETCH c.engine e "
                + "JOIN FETCH e.fuel "
                + "JOIN FETCH c.color "
                + "ORDER BY c.brand.name, c.model.name ASC", Car.class);
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
