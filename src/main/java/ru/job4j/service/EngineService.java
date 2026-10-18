package ru.job4j.service;

import ru.job4j.model.Engine;

import java.util.List;
import java.util.Optional;

public interface EngineService {

    Engine create(Engine engine);
    List<Engine> findAll();
    Optional<Engine> findById(Integer engineId);

    Engine update(Engine engine);
}
