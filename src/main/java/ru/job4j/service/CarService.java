package ru.job4j.service;

import ru.job4j.model.Car;
import ru.job4j.model.Engine;

import java.util.List;
import java.util.Optional;

public interface CarService {
    Car create(Car car);

    List<Car> findAll();
    Optional<Car> findById(Integer cardId);

    List<Car> findByLikeModelName(String modelName);

    List<Car> findByLikeBrandName(String brandName);

    Car update(Car car);

    boolean delete(Integer carId);

    boolean addOwner(Integer carId, Integer ownerId);
    boolean removeOwner(Integer carId, Integer ownerId);
}
