package ru.job4j.service;

import ru.job4j.model.Color;

import java.util.List;
import java.util.Optional;

public interface ColorService {

    List<Color> findAll();
    Optional<Color> findById(Integer colorId);
}
