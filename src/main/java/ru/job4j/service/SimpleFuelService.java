package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.model.Fuel;
import ru.job4j.repository.FuelRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleFuelService implements FuelService {
    private final FuelRepository fuelRepository;

    @Override
    public List<Fuel> findAll() {
        return fuelRepository.findAll();
    }

    @Override
    public Optional<Fuel> findById(Integer fuelId) {
        return fuelRepository.findById(fuelId);
    }
}
