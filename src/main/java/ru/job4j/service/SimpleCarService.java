package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.model.Car;
import ru.job4j.repository.CarRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleCarService implements CarService {
    private final CarRepository carRepository;

    @Override
    public Car create(Car car) {
        return carRepository.create(car);
    }

    @Override
    public Optional<Car> findById(Integer cardId) {
        return carRepository.findById(cardId);
    }

    @Override
    public Car update(Car car) {
        return carRepository.update(car);
    }

    @Override
    public boolean delete(Integer carId) {
        return carRepository.delete(carId);
    }

    @Override
    public List<Car> findByLikeModelName(String modelName) {
        return carRepository.findByLikeModelName(modelName);
    }

    @Override
    public List<Car> findByLikeBrandName(String brandName) {
        return carRepository.findByLikeBrandName(brandName);
    }

    @Override
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    @Override
    public boolean addOwner(Integer carId, Integer ownerId) {
        return carRepository.addOwner(carId, ownerId);
    }

    @Override
    public boolean removeOwner(Integer carId, Integer ownerId) {
        return carRepository.removeOwner(carId, ownerId);
    }
}
