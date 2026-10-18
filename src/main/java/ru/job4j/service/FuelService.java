package ru.job4j.service;

import ru.job4j.model.Fuel;

import java.util.List;
import java.util.Optional;

public interface FuelService {
    List<Fuel> findAll();
    Optional<Fuel> findById(Integer fuelId);
}
